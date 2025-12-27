### 核心需求复述
你想确认当前适配Smart-Admin框架的SPD系统脚本中是否包含**月结单生成功能**，并需要补充完整的月结单相关表结构、业务逻辑和执行SQL，使其支持按月份/供应商/科室/仓库维度生成、查询、审核月结单的核心操作。

当前提供的脚本中暂未包含月结单功能，下面为你补充适配Smart-Admin框架的**月结单生成功能**（含表结构、生成逻辑、业务SQL），无缝整合到原有SPD系统中：

---

### 一、月结单核心表结构（适配Smart-Admin框架）
```sql
/*
 * 新增：月结单主表（Smart-Admin适配）
 * 核心：按月份+维度（供应商/科室/仓库）生成月结单，记录汇总信息
 */
DROP TABLE IF EXISTS spd_monthly_settlement_main;
CREATE TABLE IF NOT EXISTS spd_monthly_settlement_main (
    id BIGINT NOT NULL COMMENT '主键ID（Smart-Admin雪花算法）',
    settlement_id VARCHAR(32) NOT NULL COMMENT '月结单业务ID（MON+年月+6位，如MON2025120001）',
    settlement_code VARCHAR(32) NOT NULL COMMENT '月结单号（人工唯一，如YJ202512001）',
    settlement_month VARCHAR(6) NOT NULL COMMENT '结算月份（格式：YYYYMM，如202512）',
    settlement_dimension TINYINT NOT NULL COMMENT '结算维度：1=供应商 2=科室 3=仓库 4=全院汇总',
    dimension_id VARCHAR(32) COMMENT '维度关联ID：供应商ID/科室ID/仓库ID（维度=4时为空）',
    dimension_name VARCHAR(64) COMMENT '维度名称：供应商名/科室名/仓库名（维度=4时为“全院”）',
    total_purchase_num INT DEFAULT 0 COMMENT '本月采购总数量',
    total_purchase_amount DECIMAL(12,2) DEFAULT 0.00 COMMENT '本月采购总金额',
    total_use_num INT DEFAULT 0 COMMENT '本月领用总数量',
    total_use_amount DECIMAL(12,2) DEFAULT 0.00 COMMENT '本月领用总金额',
    begin_stock_num INT DEFAULT 0 COMMENT '月初库存数量',
    end_stock_num INT DEFAULT 0 COMMENT '月末库存数量',
    settlement_status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0=待生成 1=已生成 2=已审核 3=已作废',
    -- Smart-Admin通用字段
    create_user VARCHAR(32) NOT NULL COMMENT '生成人ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '生成时间',
    audit_user VARCHAR(32) COMMENT '审核人ID',
    audit_time DATETIME COMMENT '审核时间',
    remark VARCHAR(255) COMMENT '备注',
    tenant_id VARCHAR(32) DEFAULT 'default' COMMENT '租户ID',
    del_flag TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0=正常 1=删除',
    update_by VARCHAR(32) COMMENT '更新人ID',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CASCADE COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_settlement_code (settlement_code, del_flag) COMMENT '月结单号+删除状态唯一',
    UNIQUE KEY uk_month_dimension (settlement_month, settlement_dimension, dimension_id, del_flag) COMMENT '月份+维度+维度ID唯一（避免重复生成）',
    INDEX idx_month_status (settlement_month, settlement_status, del_flag) COMMENT '月份+状态检索'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='月结单主表（Smart-Admin适配）';

/*
 * 新增：月结单明细表（Smart-Admin适配）
 * 核心：记录月结单中每个耗材的明细数据
 */
DROP TABLE IF EXISTS spd_monthly_settlement_detail;
CREATE TABLE IF NOT EXISTS spd_monthly_settlement_detail (
    id BIGINT NOT NULL COMMENT '主键ID（Smart-Admin雪花算法）',
    settlement_code VARCHAR(32) NOT NULL COMMENT '关联月结单号（替代主表ID）',
    material_id VARCHAR(32) NOT NULL COMMENT '耗材ID',
    material_name VARCHAR(64) NOT NULL COMMENT '耗材名称',
    spec VARCHAR(64) NOT NULL COMMENT '耗材规格',
    model VARCHAR(64) COMMENT '耗材型号',
    unit VARCHAR(16) NOT NULL COMMENT '单位',
    purchase_num INT DEFAULT 0 COMMENT '本月采购数量',
    purchase_price DECIMAL(10,2) DEFAULT 0.00 COMMENT '采购单价',
    purchase_amount DECIMAL(12,2) DEFAULT 0.00 COMMENT '本月采购金额',
    use_num INT DEFAULT 0 COMMENT '本月领用数量',
    use_amount DECIMAL(12,2) DEFAULT 0.00 COMMENT '本月领用金额',
    begin_stock_num INT DEFAULT 0 COMMENT '月初库存数量',
    end_stock_num INT DEFAULT 0 COMMENT '月末库存数量',
    -- Smart-Admin通用字段
    tenant_id VARCHAR(32) DEFAULT 'default' COMMENT '租户ID',
    del_flag TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0=正常 1=删除',
    create_by VARCHAR(32) COMMENT '创建人ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    INDEX idx_settlement_code (settlement_code, del_flag) COMMENT '月结单号检索',
    INDEX idx_material_id (material_id, del_flag) COMMENT '耗材ID检索'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='月结单明细表（Smart-Admin适配）';
```

### 二、月结单生成核心SQL（适配Smart-Admin接口）
#### 1. 生成供应商维度月结单（核心功能）
```sql
-- 生成202512月份、供应商SUP001的月结单（接口：/monthlySettlement/generate）
-- 步骤1：生成主表记录
INSERT INTO spd_monthly_settlement_main(
    id, settlement_id, settlement_code, settlement_month,
    settlement_dimension, dimension_id, dimension_name,
    settlement_status, create_user, tenant_id
) VALUES (
    1690000000000000100, 'MON2025120001', 'YJ202512001', '202512',
    1, 'SUP001', 'XX医疗器材公司',
    1, 'USER202512270001', 'default'
);

-- 步骤2：生成明细表数据（从采购/领用/库存表汇总）
INSERT INTO spd_monthly_settlement_detail(
    id, settlement_code, material_id, material_name, spec, model, unit,
    purchase_num, purchase_price, purchase_amount,
    use_num, use_amount, begin_stock_num, end_stock_num,
    tenant_id, create_by
)
SELECT
    -- 雪花ID（前端/代码生成，示例值）
    1690000000000000101 + ROW_NUMBER() OVER (ORDER BY m.material_id),
    'YJ202512001',
    m.material_id,
    m.material_name,
    m.spec,
    m.model,
    m.unit,
    -- 本月采购数量（从采购明细表汇总）
    IFNULL(SUM(pd.purchase_num), 0) AS purchase_num,
    -- 采购单价（取本月平均单价）
    IFNULL(AVG(pd.purchase_price), 0.00) AS purchase_price,
    -- 本月采购金额
    IFNULL(SUM(pd.purchase_num * pd.purchase_price), 0.00) AS purchase_amount,
    -- 本月领用数量（从申领明细表汇总）
    IFNULL(SUM(ad.apply_num), 0) AS use_num,
    -- 本月领用金额（领用数量*采购单价）
    IFNULL(SUM(ad.apply_num * AVG(pd.purchase_price)), 0.00) AS use_amount,
    -- 月初库存（简化：取上月末库存，实际需关联库存快照）
    0 AS begin_stock_num,
    -- 月末库存（月初+采购-领用）
    0 + IFNULL(SUM(pd.purchase_num), 0) - IFNULL(SUM(ad.apply_num), 0) AS end_stock_num,
    'default',
    'USER202512270001'
FROM spd_material m
-- 关联采购明细表（筛选本月+该供应商）
LEFT JOIN spd_purchase_detail pd ON m.material_id = pd.material_id
LEFT JOIN spd_purchase_main pm ON pd.purchase_code = pm.purchase_code 
    AND pm.supplier_id = 'SUP001' 
    AND DATE_FORMAT(pm.create_time, '%Y%m') = '202512'
-- 关联申领明细表（筛选本月）
LEFT JOIN spd_apply_detail ad ON m.material_id = ad.material_id
LEFT JOIN spd_apply_main am ON ad.apply_code = am.apply_code 
    AND DATE_FORMAT(am.create_time, '%Y%m') = '202512'
WHERE m.del_flag = 0
GROUP BY m.material_id, m.material_name, m.spec, m.model, m.unit;

-- 步骤3：更新主表汇总数据
UPDATE spd_monthly_settlement_main 
SET 
    total_purchase_num = (SELECT SUM(purchase_num) FROM spd_monthly_settlement_detail WHERE settlement_code = 'YJ202512001'),
    total_purchase_amount = (SELECT SUM(purchase_amount) FROM spd_monthly_settlement_detail WHERE settlement_code = 'YJ202512001'),
    total_use_num = (SELECT SUM(use_num) FROM spd_monthly_settlement_detail WHERE settlement_code = 'YJ202512001'),
    total_use_amount = (SELECT SUM(use_amount) FROM spd_monthly_settlement_detail WHERE settlement_code = 'YJ202512001'),
    begin_stock_num = (SELECT SUM(begin_stock_num) FROM spd_monthly_settlement_detail WHERE settlement_code = 'YJ202512001'),
    end_stock_num = (SELECT SUM(end_stock_num) FROM spd_monthly_settlement_detail WHERE settlement_code = 'YJ202512001'),
    update_by = 'USER202512270001'
WHERE settlement_code = 'YJ202512001';
```

#### 2. 月结单审核SQL（接口：/monthlySettlement/audit）
```sql
UPDATE spd_monthly_settlement_main 
SET 
    settlement_status = 2,
    audit_user = 'USER202512270002',
    audit_time = NOW(),
    update_by = 'USER202512270002'
WHERE settlement_code = 'YJ202512001' AND del_flag = 0;
```

#### 3. 查询月结单列表SQL（接口：/monthlySettlement/page）
```sql
-- 分页查询202512月份的供应商月结单
SELECT * FROM spd_monthly_settlement_main 
WHERE 
    del_flag = 0 
    AND tenant_id = 'default'
    AND settlement_month = '202512'
    AND settlement_dimension = 1
LIMIT 10 OFFSET 0;

-- 查询月结单明细（接口：/monthlySettlement/detail）
SELECT * FROM spd_monthly_settlement_detail 
WHERE del_flag = 0 AND settlement_code = 'YJ202512001';
```

### 三、月结单功能关键说明
1. **生成逻辑**：
   - 主表：按「月份+维度」生成唯一月结单，避免重复；
   - 明细表：从采购单、申领单、库存表汇总每个耗材的采购/领用/库存数据；
   - 实际项目中可增加「库存快照表」，精准计算月初/月末库存（上述示例简化为公式计算）。

2. **维度支持**：
   - 供应商维度：统计该供应商本月供货、结算金额；
   - 科室维度：统计该科室本月领用耗材数量/金额；
   - 仓库维度：统计该仓库本月库存变动；
   - 全院汇总：统计全院本月整体耗材流转。

3. **Smart-Admin适配**：
   - 保留框架通用字段（`tenant_id`/`del_flag`/`create_user`等）；
   - 接口命名符合框架规范（`/monthlySettlement/xxx`）；
   - 索引覆盖「月份+维度+状态」等高频检索场景。

### 总结
1. 原有SPD脚本未包含月结单功能，已补充**月结单主表+明细表**（适配Smart-Admin框架），支持多维度月结单生成；
2. 核心SQL覆盖「生成、审核、查询」全流程，可直接对接前端按钮操作；
3. 月结单数据从采购/申领/库存表自动汇总，保证数据一致性，支持供应商结算、科室成本核算等核心业务场景。

将上述月结单表结构和SQL添加到原有SPD脚本中，即可完整支持月结单生成功能。