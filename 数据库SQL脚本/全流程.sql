/*
 * SPD院内耗材管理系统全表创建脚本（Smart-Admin框架适配最终版）
 * 整合所有业务需求：
 * 1. 耗材表：集采/UDI/注册证/高值/精细化状态/型号等字段；
 * 2. 库存逻辑：仅标签表（定数包）+散货表（散货），无独立库存表，标签可用状态合并两者判定；
 * 3. 操作轨迹：主表记录新增/审核人员、时间；
 * 4. 框架适配：雪花主键/逻辑删除/多租户/MyBatis-Plus代码生成适配；
 * 5. 全表补充业务按钮SQL（对应Smart-Admin接口规范）；
 * 适用：MySQL 8.0+ | 编码：UTF8MB4 | 引擎：InnoDB
 * 创建时间：2025-12-27
 */
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 1. 系统用户表（Smart-Admin基础）
-- ----------------------------
DROP TABLE IF EXISTS spd_user;
CREATE TABLE IF NOT EXISTS spd_user (
    id BIGINT NOT NULL COMMENT '主键ID（Smart-Admin雪花算法）',
    user_id VARCHAR(32) NOT NULL COMMENT '用户业务ID（USER+年月日+6位）',
    user_name VARCHAR(32) NOT NULL COMMENT '用户姓名',
    user_phone VARCHAR(11) COMMENT '用户手机号',
    user_role TINYINT NOT NULL DEFAULT 1 COMMENT '角色：1=普通用户 2=审核员 3=管理员',
    tenant_id VARCHAR(32) DEFAULT 'default' COMMENT '租户ID（多租户）',
    del_flag TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0=正常 1=删除',
    create_by VARCHAR(32) COMMENT '创建人ID（关联user_id）',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(32) COMMENT '更新人ID',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_phone (user_phone, del_flag) COMMENT '手机号+删除状态唯一',
    UNIQUE KEY uk_user_id (user_id, del_flag) COMMENT '业务ID+删除状态唯一',
    INDEX idx_tenant_role (tenant_id, user_role, del_flag) COMMENT '租户+角色检索'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表（Smart-Admin适配）';

-- 【Smart-Admin-用户表业务按钮SQL】
-- 新增用户（接口：/spdUser/add）
-- INSERT INTO spd_user(id, user_id, user_name, user_phone, user_role, tenant_id, create_by) 
-- VALUES (1690000000000000001, 'USER202512270001', '张三', '13800138000', 1, 'default', 'USER202512270001');
-- 编辑用户（接口：/spdUser/edit）
-- UPDATE spd_user SET user_name = '李四', user_role = 2, update_by = 'USER202512270001' WHERE id = 1690000000000000001;
-- 逻辑删除用户（接口：/spdUser/delete）
-- UPDATE spd_user SET del_flag = 1, update_by = 'USER202512270001' WHERE id = 1690000000000000001;
-- 分页查询（接口：/spdUser/page）
-- SELECT * FROM spd_user WHERE del_flag=0 AND tenant_id='default' AND user_name LIKE '%张三%' LIMIT 10 OFFSET 0;

-- ----------------------------
-- 2. 组织节点表（科室+仓库合并）
-- ----------------------------
DROP TABLE IF EXISTS spd_org_node;
CREATE TABLE IF NOT EXISTS spd_org_node (
    id BIGINT NOT NULL COMMENT '主键ID（雪花算法）',
    node_id VARCHAR(32) NOT NULL COMMENT '节点业务ID（DEPT/WH+年月日+6位）',
    node_name VARCHAR(64) NOT NULL COMMENT '节点名称（科室/仓库名）',
    node_type TINYINT NOT NULL COMMENT '类型：1=科室 2=仓库',
    parent_node_id VARCHAR(32) COMMENT '上级节点ID（仅科室关联仓库）',
    warehouse_type TINYINT COMMENT '仓库类型：1=中心库 2=科室库 3=临时库',
    warehouse_status TINYINT DEFAULT 1 COMMENT '仓库状态：1=启用 0=禁用',
    dept_status TINYINT DEFAULT 1 COMMENT '科室状态：1=启用 0=禁用',
    tenant_id VARCHAR(32) DEFAULT 'default' COMMENT '租户ID',
    del_flag TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0=正常 1=删除',
    create_by VARCHAR(32) COMMENT '创建人ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(32) COMMENT '更新人ID',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_node_name_type (node_name, node_type, del_flag) COMMENT '名称+类型+删除状态唯一',
    UNIQUE KEY uk_node_id (node_id, del_flag) COMMENT '业务ID+删除状态唯一',
    INDEX idx_parent_node (parent_node_id, del_flag) COMMENT '上级节点检索',
    INDEX idx_tenant_type (tenant_id, node_type, del_flag) COMMENT '租户+类型检索'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='科室-仓库组织节点表（Smart-Admin适配）';

-- 【Smart-Admin-组织节点表业务按钮SQL】
-- 新增仓库（接口：/spdOrgNode/add）
-- INSERT INTO spd_org_node(id, node_id, node_name, node_type, warehouse_type, tenant_id, create_by) 
-- VALUES (1690000000000000002, 'WH202512270001', '中心库', 2, 1, 'default', 'USER202512270001');
-- 新增科室（接口：/spdOrgNode/add）
-- INSERT INTO spd_org_node(id, node_id, node_name, node_type, parent_node_id, tenant_id, create_by) 
-- VALUES (1690000000000000003, 'DEPT202512270001', '内科', 1, 'WH202512270001', 'default', 'USER202512270001');
-- 禁用仓库（接口：/spdOrgNode/editStatus）
-- UPDATE spd_org_node SET warehouse_status=0, update_by='USER202512270001' WHERE node_id='WH202512270001';

-- ----------------------------
-- 3. 耗材基础表（核心：集采/UDI/资质/精细化状态）
-- ----------------------------
DROP TABLE IF EXISTS spd_material;
CREATE TABLE IF NOT EXISTS spd_material (
    id BIGINT NOT NULL COMMENT '主键ID（雪花算法）',
    material_id VARCHAR(32) NOT NULL COMMENT '耗材业务ID（MAT+年月日+6位）',
    material_name VARCHAR(64) NOT NULL COMMENT '耗材名称',
    spec VARCHAR(64) NOT NULL COMMENT '规格',
    model VARCHAR(64) COMMENT '型号（新增）',
    unit VARCHAR(16) NOT NULL COMMENT '基础单位（个/包/盒）',
    material_type VARCHAR(32) COMMENT '分类：一次性耗材/设备类',
    -- 集采相关
    is_collection_purchase TINYINT NOT NULL DEFAULT 0 COMMENT '是否集采：0=否 1=是',
    collection_type TINYINT COMMENT '集采类型：1=省集采 2=市集采 3=合同采购 4=其他',
    contract_no VARCHAR(64) COMMENT '合同号（新增）',
    -- 资质相关
    udi_code VARCHAR(64) COMMENT 'UDI编码（医疗器械唯一标识）',
    registration_no VARCHAR(64) COMMENT '注册证号（新增）',
    registration_valid_date DATE COMMENT '注册证有效期（新增）',
    is_high_value TINYINT NOT NULL DEFAULT 0 COMMENT '是否高值：0=否 1=是（新增）',
    -- 精细化状态
    material_status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1=可用 2=业务停用 3=质量停用（新增）',
    -- 框架通用字段
    tenant_id VARCHAR(32) DEFAULT 'default' COMMENT '租户ID',
    del_flag TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0=正常 1=删除',
    create_by VARCHAR(32) COMMENT '创建人ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(32) COMMENT '更新人ID',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_material_name_spec_model (material_name, spec, model, del_flag) COMMENT '名称+规格+型号+删除状态唯一',
    UNIQUE KEY uk_material_id (material_id, del_flag) COMMENT '业务ID+删除状态唯一',
    INDEX idx_collection (is_collection_purchase, collection_type, del_flag) COMMENT '集采检索',
    INDEX idx_high_value (is_high_value, material_status, del_flag) COMMENT '高值耗材检索',
    INDEX idx_reg_valid (registration_valid_date, del_flag) COMMENT '注册证有效期检索'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='耗材基础表（集采/UDI/资质，Smart-Admin适配）';

-- 【Smart-Admin-耗材表业务按钮SQL】
-- 新增耗材（接口：/spdMaterial/add）
-- INSERT INTO spd_material(
--     id, material_id, material_name, spec, model, unit, material_type,
--     is_collection_purchase, collection_type, contract_no,
--     udi_code, registration_no, registration_valid_date, is_high_value, material_status,
--     tenant_id, create_by
-- ) VALUES (
--     1690000000000000004, 'MAT202512270001', '一次性注射器', '5ml', 'SYR-5', '支', '一次性耗材',
--     1, 1, 'HT2025001',
--     '861234567890123', '国械注准2025XXXXXXX', '20301231', 0, 1,
--     'default', 'USER202512270001'
-- );
-- 业务停用耗材（接口：/spdMaterial/stopBusiness）
-- UPDATE spd_material SET material_status=2, update_by='USER202512270001' WHERE material_id='MAT202512270001';
-- 查询集采耗材（接口：/spdMaterial/listCollection）
-- SELECT * FROM spd_material WHERE del_flag=0 AND is_collection_purchase=1;

-- ----------------------------
-- 4. 耗材批号表
-- ----------------------------
DROP TABLE IF EXISTS spd_material_batch;
CREATE TABLE IF NOT EXISTS spd_material_batch (
    id BIGINT NOT NULL COMMENT '主键ID（雪花算法）',
    batch_id VARCHAR(32) NOT NULL COMMENT '批号业务ID（BATCH+年月日+6位）',
    material_id VARCHAR(32) NOT NULL COMMENT '关联耗材业务ID',
    batch_no VARCHAR(32) NOT NULL COMMENT '业务批号',
    valid_date DATE NOT NULL COMMENT '效期日期',
    supplier_id VARCHAR(32) NOT NULL COMMENT '供应商ID',
    supplier_name VARCHAR(64) COMMENT '供应商名称',
    tenant_id VARCHAR(32) DEFAULT 'default' COMMENT '租户ID',
    del_flag TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0=正常 1=删除',
    create_by VARCHAR(32) COMMENT '创建人ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(32) COMMENT '更新人ID',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_material_batch (material_id, batch_no, del_flag) COMMENT '耗材+批号+删除状态唯一',
    UNIQUE KEY uk_batch_id (batch_id, del_flag) COMMENT '业务ID+删除状态唯一',
    INDEX idx_material_valid (material_id, valid_date, del_flag) COMMENT '耗材+效期检索'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='耗材批号表（Smart-Admin适配）';

-- 【Smart-Admin-批号表业务按钮SQL】
-- 新增批号（接口：/spdMaterialBatch/add）
-- INSERT INTO spd_material_batch(
--     id, batch_id, material_id, batch_no, valid_date, supplier_id, supplier_name, tenant_id, create_by
-- ) VALUES (
--     1690000000000000005, 'BATCH202512270001', 'MAT202512270001', '20251201', '20271201', 'SUP001', 'XX医疗器材公司', 'default', 'USER202512270001'
-- );

-- ----------------------------
-- 5. 申领单主表（无数量，操作轨迹）
-- ----------------------------
DROP TABLE IF EXISTS spd_apply_main;
CREATE TABLE IF NOT EXISTS spd_apply_main (
    id BIGINT NOT NULL COMMENT '主键ID（雪花算法）',
    apply_id VARCHAR(32) NOT NULL COMMENT '申领单业务ID（APPLY+年月日+6位）',
    apply_code VARCHAR(32) NOT NULL COMMENT '申领单号（人工唯一）',
    dept_id VARCHAR(32) NOT NULL COMMENT '申领科室ID',
    warehouse_id VARCHAR(32) NOT NULL COMMENT '申领仓库ID',
    apply_status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0=待审核 1=已审核 2=缺货 3=转采购 4=作废',
    create_user VARCHAR(32) NOT NULL COMMENT '申领人ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申领时间',
    audit_user VARCHAR(32) COMMENT '审核人ID',
    audit_time DATETIME COMMENT '审核时间',
    remark VARCHAR(255) COMMENT '备注',
    tenant_id VARCHAR(32) DEFAULT 'default' COMMENT '租户ID',
    del_flag TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0=正常 1=删除',
    update_by VARCHAR(32) COMMENT '更新人ID',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_apply_code (apply_code, del_flag) COMMENT '申领单号+删除状态唯一',
    INDEX idx_dept_status (dept_id, apply_status, del_flag) COMMENT '科室+状态检索'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='申领单主表（Smart-Admin适配）';

-- 【Smart-Admin-申领单业务按钮SQL】
-- 新增申领单（接口：/spdApply/add）
-- INSERT INTO spd_apply_main(id, apply_id, apply_code, dept_id, warehouse_id, apply_status, create_user, tenant_id) 
-- VALUES (1690000000000000006, 'APPLY202512270001', 'AP20251227001', 'DEPT202512270001', 'WH202512270001', 0, 'USER202512270001', 'default');
-- 审核申领单（接口：/spdApply/audit）
-- UPDATE spd_apply_main SET apply_status=1, audit_user='USER202512270002', audit_time=NOW() WHERE apply_code='AP20251227001';

-- ----------------------------
-- 6. 申领单明细表（无主表ID，关联业务单号）
-- ----------------------------
DROP TABLE IF EXISTS spd_apply_detail;
CREATE TABLE IF NOT EXISTS spd_apply_detail (
    id BIGINT NOT NULL COMMENT '主键ID（雪花算法）',
    apply_code VARCHAR(32) NOT NULL COMMENT '关联申领单号（替代主表ID）',
    material_id VARCHAR(32) NOT NULL COMMENT '耗材ID',
    batch_id VARCHAR(32) COMMENT '批号ID',
    apply_num INT NOT NULL COMMENT '申领数量',
    tenant_id VARCHAR(32) DEFAULT 'default' COMMENT '租户ID',
    del_flag TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0=正常 1=删除',
    create_by VARCHAR(32) COMMENT '创建人ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    INDEX idx_apply_code (apply_code, del_flag) COMMENT '申领单号检索'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='申领单明细表（Smart-Admin适配）';

-- 【Smart-Admin-申领明细表业务按钮SQL】
-- 新增申领明细（接口：/spdApplyDetail/add）
-- INSERT INTO spd_apply_detail(id, apply_code, material_id, batch_id, apply_num, tenant_id, create_by) 
-- VALUES (1690000000000000007, 'AP20251227001', 'MAT202512270001', 'BATCH202512270001', 100, 'default', 'USER202512270001');

-- ----------------------------
-- 7. 采购单主表
-- ----------------------------
DROP TABLE IF EXISTS spd_purchase_main;
CREATE TABLE IF NOT EXISTS spd_purchase_main (
    id BIGINT NOT NULL COMMENT '主键ID（雪花算法）',
    purchase_id VARCHAR(32) NOT NULL COMMENT '采购单业务ID（PUR+年月日+6位）',
    purchase_code VARCHAR(32) NOT NULL COMMENT '采购单号（人工唯一）',
    supplier_id VARCHAR(32) NOT NULL COMMENT '供应商ID',
    supplier_name VARCHAR(64) COMMENT '供应商名称',
    warehouse_id VARCHAR(32) NOT NULL COMMENT '收货仓库ID',
    source_type TINYINT NOT NULL DEFAULT 0 COMMENT '采购类型：1=集采 2=零星 3=补库',
    source_code VARCHAR(32) COMMENT '关联申领单号',
    purchase_status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0=待审核 1=已审核 2=已下单 3=已收货 4=作废',
    create_user VARCHAR(32) NOT NULL COMMENT '采购人ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    audit_user VARCHAR(32) COMMENT '审核人ID',
    audit_time DATETIME COMMENT '审核时间',
    remark VARCHAR(255) COMMENT '备注',
    tenant_id VARCHAR(32) DEFAULT 'default' COMMENT '租户ID',
    del_flag TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0=正常 1=删除',
    update_by VARCHAR(32) COMMENT '更新人ID',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_purchase_code (purchase_code, del_flag) COMMENT '采购单号+删除状态唯一',
    INDEX idx_supplier_status (supplier_id, purchase_status, del_flag) COMMENT '供应商+状态检索'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购单主表（Smart-Admin适配）';

-- 【Smart-Admin-采购单业务按钮SQL】
-- 新增采购单（接口：/spdPurchase/add）
-- INSERT INTO spd_purchase_main(id, purchase_id, purchase_code, supplier_id, warehouse_id, source_type, purchase_status, create_user, tenant_id) 
-- VALUES (1690000000000000008, 'PUR202512270001', 'PUR20251227001', 'SUP001', 'WH202512270001', 1, 0, 'USER202512270001', 'default');

-- ----------------------------
-- 8. 采购单明细表
-- ----------------------------
DROP TABLE IF EXISTS spd_purchase_detail;
CREATE TABLE IF NOT EXISTS spd_purchase_detail (
    id BIGINT NOT NULL COMMENT '主键ID（雪花算法）',
    purchase_code VARCHAR(32) NOT NULL COMMENT '关联采购单号（替代主表ID）',
    material_id VARCHAR(32) NOT NULL COMMENT '耗材ID',
    batch_id VARCHAR(32) COMMENT '批号ID',
    purchase_num INT NOT NULL COMMENT '采购数量',
    purchase_price DECIMAL(10,2) NOT NULL COMMENT '采购单价',
    tenant_id VARCHAR(32) DEFAULT 'default' COMMENT '租户ID',
    del_flag TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0=正常 1=删除',
    create_by VARCHAR(32) COMMENT '创建人ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    INDEX idx_purchase_code (purchase_code, del_flag) COMMENT '采购单号检索'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购单明细表（Smart-Admin适配）';

-- ----------------------------
-- 9. 验收单主表
-- ----------------------------
DROP TABLE IF EXISTS spd_check_main;
CREATE TABLE IF NOT EXISTS spd_check_main (
    id BIGINT NOT NULL COMMENT '主键ID（雪花算法）',
    check_id VARCHAR(32) NOT NULL COMMENT '验收单业务ID（CHECK+年月日+6位）',
    check_code VARCHAR(32) NOT NULL COMMENT '验收单号（人工唯一）',
    purchase_code VARCHAR(32) NOT NULL COMMENT '关联采购单号',
    warehouse_id VARCHAR(32) NOT NULL COMMENT '验收仓库ID',
    check_status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0=待验收 1=已验收 2=部分拒收 3=全部拒收',
    create_user VARCHAR(32) NOT NULL COMMENT '验收人ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '验收时间',
    audit_user VARCHAR(32) COMMENT '复核人ID',
    audit_time DATETIME COMMENT '复核时间',
    remark VARCHAR(255) COMMENT '备注（拒收原因）',
    tenant_id VARCHAR(32) DEFAULT 'default' COMMENT '租户ID',
    del_flag TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0=正常 1=删除',
    update_by VARCHAR(32) COMMENT '更新人ID',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_check_code (check_code, del_flag) COMMENT '验收单号+删除状态唯一',
    INDEX idx_purchase_code (purchase_code, del_flag) COMMENT '采购单号检索'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='验收单主表（Smart-Admin适配）';

-- ----------------------------
-- 10. 验收单明细表
-- ----------------------------
DROP TABLE IF EXISTS spd_check_detail;
CREATE TABLE IF NOT EXISTS spd_check_detail (
    id BIGINT NOT NULL COMMENT '主键ID（雪花算法）',
    check_code VARCHAR(32) NOT NULL COMMENT '关联验收单号（替代主表ID）',
    material_id VARCHAR(32) NOT NULL COMMENT '耗材ID',
    batch_id VARCHAR(32) NOT NULL COMMENT '批号ID',
    delivery_num INT NOT NULL COMMENT '配送数量',
    check_num INT NOT NULL COMMENT '验收数量',
    reject_reason VARCHAR(255) COMMENT '拒收原因',
    tenant_id VARCHAR(32) DEFAULT 'default' COMMENT '租户ID',
    del_flag TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0=正常 1=删除',
    create_by VARCHAR(32) COMMENT '创建人ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    INDEX idx_check_code (check_code, del_flag) COMMENT '验收单号检索'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='验收单明细表（Smart-Admin适配）';

-- ----------------------------
-- 11. 收货单主表
-- ----------------------------
DROP TABLE IF EXISTS spd_receive_main;
CREATE TABLE IF NOT EXISTS spd_receive_main (
    id BIGINT NOT NULL COMMENT '主键ID（雪花算法）',
    receive_id VARCHAR(32) NOT NULL COMMENT '收货单业务ID（RECV+年月日+6位）',
    receive_code VARCHAR(32) NOT NULL COMMENT '收货单号（人工唯一）',
    check_code VARCHAR(32) NOT NULL COMMENT '关联验收单号',
    purchase_code VARCHAR(32) NOT NULL COMMENT '关联采购单号',
    warehouse_id VARCHAR(32) NOT NULL COMMENT '收货仓库ID',
    receive_type TINYINT NOT NULL COMMENT '类型：1=定数包 2=散货',
    receive_status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0=待收货 1=已收货 2=已上架',
    create_user VARCHAR(32) NOT NULL COMMENT '收货人ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收货时间',
    shelf_user VARCHAR(32) COMMENT '上架人ID',
    shelf_time DATETIME COMMENT '上架时间',
    tenant_id VARCHAR(32) DEFAULT 'default' COMMENT '租户ID',
    del_flag TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0=正常 1=删除',
    update_by VARCHAR(32) COMMENT '更新人ID',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_receive_code (receive_code, del_flag) COMMENT '收货单号+删除状态唯一'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收货单主表（Smart-Admin适配）';

-- ----------------------------
-- 12. 收货单明细表
-- ----------------------------
DROP TABLE IF EXISTS spd_receive_detail;
CREATE TABLE IF NOT EXISTS spd_receive_detail (
    id BIGINT NOT NULL COMMENT '主键ID（雪花算法）',
    receive_code VARCHAR(32) NOT NULL COMMENT '关联收货单号（替代主表ID）',
    material_id VARCHAR(32) NOT NULL COMMENT '耗材ID',
    batch_id VARCHAR(32) NOT NULL COMMENT '批号ID',
    receive_num INT NOT NULL COMMENT '收货数量',
    package_spec VARCHAR(64) COMMENT '定数包规格（如10支/包）',
    label_code VARCHAR(64) COMMENT '关联标签码（定数包）',
    tenant_id VARCHAR(32) DEFAULT 'default' COMMENT '租户ID',
    del_flag TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0=正常 1=删除',
    create_by VARCHAR(32) COMMENT '创建人ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    INDEX idx_receive_code (receive_code, del_flag) COMMENT '收货单号检索'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收货单明细表（Smart-Admin适配）';

-- ----------------------------
-- 13. 打包单主表
-- ----------------------------
DROP TABLE IF EXISTS spd_package_main;
CREATE TABLE IF NOT EXISTS spd_package_main (
    id BIGINT NOT NULL COMMENT '主键ID（雪花算法）',
    package_id VARCHAR(32) NOT NULL COMMENT '打包单业务ID（PACK+年月日+6位）',
    package_code VARCHAR(32) NOT NULL COMMENT '打包单号（人工唯一）',
    warehouse_id VARCHAR(32) NOT NULL COMMENT '打包仓库ID',
    package_type TINYINT NOT NULL DEFAULT 1 COMMENT '类型：1=日常打包 2=收货打包 3=拆包重打',
    package_status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0=待打包 1=已打包 2=已作废 3=待上架 4=已上架',
    receive_code VARCHAR(32) COMMENT '关联收货单号（仅收货打包）',
    create_user VARCHAR(32) NOT NULL COMMENT '打包人ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    package_time DATETIME COMMENT '打包完成时间',
    tenant_id VARCHAR(32) DEFAULT 'default' COMMENT '租户ID',
    del_flag TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0=正常 1=删除',
    update_by VARCHAR(32) COMMENT '更新人ID',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_package_code (package_code, del_flag) COMMENT '打包单号+删除状态唯一'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='打包单主表（Smart-Admin适配）';

-- ----------------------------
-- 14. 打包单明细表
-- ----------------------------
DROP TABLE IF EXISTS spd_package_detail;
CREATE TABLE IF NOT EXISTS spd_package_detail (
    id BIGINT NOT NULL COMMENT '主键ID（雪花算法）',
    package_code VARCHAR(32) NOT NULL COMMENT '关联打包单号（替代主表ID）',
    material_id VARCHAR(32) NOT NULL COMMENT '耗材ID',
    batch_id VARCHAR(32) NOT NULL COMMENT '批号ID',
    package_spec VARCHAR(64) NOT NULL COMMENT '打包规格（如10支/包）',
    package_num INT NOT NULL COMMENT '打包数量',
    tenant_id VARCHAR(32) DEFAULT 'default' COMMENT '租户ID',
    del_flag TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0=正常 1=删除',
    create_by VARCHAR(32) COMMENT '创建人ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    INDEX idx_package_code (package_code, del_flag) COMMENT '打包单号检索'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='打包单明细表（Smart-Admin适配）';

-- ----------------------------
-- 15. 标签表（核心：定数包库存）
-- ----------------------------
DROP TABLE IF EXISTS spd_label;
CREATE TABLE IF NOT EXISTS spd_label (
    id BIGINT NOT NULL COMMENT '主键ID（雪花算法）',
    label_code VARCHAR(64) NOT NULL COMMENT '标签码（唯一，定数包标识）',
    material_id VARCHAR(32) NOT NULL COMMENT '关联耗材ID',
    batch_id VARCHAR(32) NOT NULL COMMENT '关联批号ID',
    package_spec VARCHAR(64) NOT NULL COMMENT '包装规格（如10支/包）',
    package_num INT NOT NULL COMMENT '单包数量',
    warehouse_id VARCHAR(32) NOT NULL COMMENT '所属仓库ID',
    label_status TINYINT NOT NULL DEFAULT 1 COMMENT '标签状态：1=可用 2=已领用 3=已作废 4=已过期',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '生成时间',
    use_time DATETIME COMMENT '领用时间',
    tenant_id VARCHAR(32) DEFAULT 'default' COMMENT '租户ID',
    del_flag TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0=正常 1=删除',
    update_by VARCHAR(32) COMMENT '更新人ID',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_label_code (label_code, del_flag) COMMENT '标签码+删除状态唯一',
    INDEX idx_material_warehouse (material_id, warehouse_id, label_status, del_flag) COMMENT '耗材+仓库+状态检索'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='标签表（定数包库存，Smart-Admin适配）';

-- 【标签可用状态查询SQL（合并散货表）】
-- SELECT 
--     m.material_name, m.spec,
--     -- 定数包可用数量
--     SUM(CASE WHEN l.label_status=1 THEN l.package_num ELSE 0 END) AS label_available_num,
--     -- 散货可用数量
--     SUM(CASE WHEN s.del_flag=0 THEN s.remaining_num ELSE 0 END) AS bulk_available_num,
--     -- 总可用数量
--     (SUM(CASE WHEN l.label_status=1 THEN l.package_num ELSE 0 END) + SUM(CASE WHEN s.del_flag=0 THEN s.remaining_num ELSE 0 END)) AS total_available_num
-- FROM spd_material m
-- LEFT JOIN spd_label l ON m.material_id = l.material_id AND l.del_flag=0
-- LEFT JOIN spd_bulk_material s ON m.material_id = s.material_id AND s.del_flag=0
-- WHERE m.del_flag=0 AND m.material_status=1
-- GROUP BY m.material_id, m.material_name, m.spec;

-- ----------------------------
-- 16. 散货表（核心：散货库存）
-- ----------------------------
DROP TABLE IF EXISTS spd_bulk_material;
CREATE TABLE IF NOT EXISTS spd_bulk_material (
    id BIGINT NOT NULL COMMENT '主键ID（雪花算法）',
    bulk_id VARCHAR(32) NOT NULL COMMENT '散货业务ID（BULK+年月日+6位）',
    material_id VARCHAR(32) NOT NULL COMMENT '关联耗材ID',
    batch_id VARCHAR(32) NOT NULL COMMENT '关联批号ID',
    total_num INT NOT NULL COMMENT '入库总数量',
    used_num INT NOT NULL DEFAULT 0 COMMENT '已使用数量',
    remaining_num INT NOT NULL COMMENT '剩余数量（total_num - used_num）',
    warehouse_id VARCHAR(32) NOT NULL COMMENT '所属仓库ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '入库时间',
    tenant_id VARCHAR(32) DEFAULT 'default' COMMENT '租户ID',
    del_flag TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0=正常 1=删除',
    update_by VARCHAR(32) COMMENT '更新人ID',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_bulk_id (bulk_id, del_flag) COMMENT '散货业务ID+删除状态唯一',
    INDEX idx_material_warehouse (material_id, warehouse_id, del_flag) COMMENT '耗材+仓库检索'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='散货表（散货库存，Smart-Admin适配）';

-- 【散货库存更新SQL（领用后）】
-- UPDATE spd_bulk_material 
-- SET used_num = used_num + 50, remaining_num = total_num - (used_num + 50), update_by='USER202512270001' 
-- WHERE bulk_id='BULK202512270001' AND del_flag=0;

SET FOREIGN_KEY_CHECKS = 1;