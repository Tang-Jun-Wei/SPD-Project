/*
 * 科室分类表与耗材分类表SQL脚本
 * 用于基础资料模块的分类维护功能
 * 支持树形结构、层级管理、拖拽排序
 */

USE smart_admin_v3;

SET NAMES utf8mb4;

-- ----------------------------
-- 科室分类表
-- ----------------------------
DROP TABLE IF EXISTS spd_dept_category;
CREATE TABLE IF NOT EXISTS spd_dept_category (
    id BIGINT NOT NULL COMMENT '主键ID（雪花算法）',
    category_id VARCHAR(32) NOT NULL COMMENT '分类业务ID（DEPTCAT+年月日+6位）',
    category_name VARCHAR(64) NOT NULL COMMENT '分类名称',
    category_code VARCHAR(32) COMMENT '分类编码',
    parent_id VARCHAR(32) COMMENT '上级分类ID（根节点为空）',
    category_level TINYINT NOT NULL DEFAULT 1 COMMENT '分类层级：1=一级 2=二级 3=三级',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '排序号（同级排序，数字越小越靠前）',
    category_status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1=启用 0=禁用',
    remark VARCHAR(255) COMMENT '备注',
    tenant_id VARCHAR(32) DEFAULT 'default' COMMENT '租户ID',
    del_flag TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0=正常 1=删除',
    create_by VARCHAR(32) COMMENT '创建人ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(32) COMMENT '更新人ID',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_category_id (category_id, del_flag) COMMENT '业务ID+删除状态唯一',
    UNIQUE KEY uk_category_name_parent (category_name, parent_id, del_flag) COMMENT '名称+父级+删除状态唯一',
    INDEX idx_parent_id (parent_id, del_flag) COMMENT '父级ID检索',
    INDEX idx_tenant_status (tenant_id, category_status, del_flag) COMMENT '租户+状态检索'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='科室分类表（支持树形结构）';

-- 【科室分类表业务SQL示例】
-- 新增一级分类（接口：/spdDeptCategory/add）
-- INSERT INTO spd_dept_category(id, category_id, category_name, category_code, parent_id, category_level, sort_order, tenant_id, create_by) 
-- VALUES (1690000000000000100, 'DEPTCAT202512270001', '临床科室', 'CLINIC', NULL, 1, 1, 'default', 'USER202512270001');

-- 新增二级分类（接口：/spdDeptCategory/add）
-- INSERT INTO spd_dept_category(id, category_id, category_name, category_code, parent_id, category_level, sort_order, tenant_id, create_by) 
-- VALUES (1690000000000000101, 'DEPTCAT202512270002', '内科', 'INTERNAL', 'DEPTCAT202512270001', 2, 1, 'default', 'USER202512270001');

-- 查询树形结构（接口：/spdDeptCategory/queryTree）
-- SELECT * FROM spd_dept_category WHERE del_flag=0 AND tenant_id='default' ORDER BY sort_order ASC;

-- 更新排序（接口：/spdDeptCategory/updateSort）
-- UPDATE spd_dept_category SET sort_order=5, update_by='USER202512270001' WHERE category_id='DEPTCAT202512270001';

-- ----------------------------
-- 耗材分类表
-- ----------------------------
DROP TABLE IF EXISTS spd_material_category;
CREATE TABLE IF NOT EXISTS spd_material_category (
    id BIGINT NOT NULL COMMENT '主键ID（雪花算法）',
    category_id VARCHAR(32) NOT NULL COMMENT '分类业务ID（MATCAT+年月日+6位）',
    category_name VARCHAR(64) NOT NULL COMMENT '分类名称',
    category_code VARCHAR(32) COMMENT '分类编码',
    parent_id VARCHAR(32) COMMENT '上级分类ID（根节点为空）',
    category_level TINYINT NOT NULL DEFAULT 1 COMMENT '分类层级：1=一级 2=二级 3=三级',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '排序号（同级排序，数字越小越靠前）',
    category_status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1=启用 0=禁用',
    remark VARCHAR(255) COMMENT '备注',
    tenant_id VARCHAR(32) DEFAULT 'default' COMMENT '租户ID',
    del_flag TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0=正常 1=删除',
    create_by VARCHAR(32) COMMENT '创建人ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(32) COMMENT '更新人ID',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_category_id (category_id, del_flag) COMMENT '业务ID+删除状态唯一',
    UNIQUE KEY uk_category_name_parent (category_name, parent_id, del_flag) COMMENT '名称+父级+删除状态唯一',
    INDEX idx_parent_id (parent_id, del_flag) COMMENT '父级ID检索',
    INDEX idx_tenant_status (tenant_id, category_status, del_flag) COMMENT '租户+状态检索'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='耗材分类表（支持树形结构）';

-- 【耗材分类表业务SQL示例】
-- 新增一级分类（接口：/spdMaterialCategory/add）
-- INSERT INTO spd_material_category(id, category_id, category_name, category_code, parent_id, category_level, sort_order, tenant_id, create_by) 
-- VALUES (1690000000000000200, 'MATCAT202512270001', '医用耗材', 'MEDICAL', NULL, 1, 1, 'default', 'USER202512270001');

-- 新增二级分类（接口：/spdMaterialCategory/add）
-- INSERT INTO spd_material_category(id, category_id, category_name, category_code, parent_id, category_level, sort_order, tenant_id, create_by) 
-- VALUES (1690000000000000201, 'MATCAT202512270002', '一次性耗材', 'DISPOSABLE', 'MATCAT202512270001', 2, 1, 'default', 'USER202512270001');

-- 初始化测试数据
INSERT INTO spd_dept_category(id, category_id, category_name, category_code, parent_id, category_level, sort_order, tenant_id, create_by) 
VALUES 
(1690000000000000100, 'DEPTCAT202512270001', '临床科室', 'CLINIC', NULL, 1, 1, 'default', '1'),
(1690000000000000101, 'DEPTCAT202512270002', '内科', 'INTERNAL', 'DEPTCAT202512270001', 2, 1, 'default', '1'),
(1690000000000000102, 'DEPTCAT202512270003', '外科', 'SURGERY', 'DEPTCAT202512270001', 2, 2, 'default', '1'),
(1690000000000000103, 'DEPTCAT202512270004', '医技科室', 'TECHNICAL', NULL, 1, 2, 'default', '1'),
(1690000000000000104, 'DEPTCAT202512270005', '检验科', 'LAB', 'DEPTCAT202512270004', 2, 1, 'default', '1'),
(1690000000000000105, 'DEPTCAT202512270006', '影像科', 'RADIOLOGY', 'DEPTCAT202512270004', 2, 2, 'default', '1');

INSERT INTO spd_material_category(id, category_id, category_name, category_code, parent_id, category_level, sort_order, tenant_id, create_by) 
VALUES 
(1690000000000000200, 'MATCAT202512270001', '医用耗材', 'MEDICAL', NULL, 1, 1, 'default', '1'),
(1690000000000000201, 'MATCAT202512270002', '一次性耗材', 'DISPOSABLE', 'MATCAT202512270001', 2, 1, 'default', '1'),
(1690000000000000202, 'MATCAT202512270003', '植入性耗材', 'IMPLANT', 'MATCAT202512270001', 2, 2, 'default', '1'),
(1690000000000000203, 'MATCAT202512270004', '医疗设备', 'EQUIPMENT', NULL, 1, 2, 'default', '1'),
(1690000000000000204, 'MATCAT202512270005', '检验试剂', 'REAGENT', 'MATCAT202512270004', 2, 1, 'default', '1');

-- 验证数据
SELECT '=== 科室分类树形数据 ===' AS INFO;
SELECT category_id, category_name, category_code, parent_id, category_level, sort_order 
FROM spd_dept_category WHERE del_flag=0 ORDER BY sort_order;

SELECT '=== 耗材分类树形数据 ===' AS INFO;
SELECT category_id, category_name, category_code, parent_id, category_level, sort_order 
FROM spd_material_category WHERE del_flag=0 ORDER BY sort_order;
