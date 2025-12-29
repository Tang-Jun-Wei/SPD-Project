/*
 * ==========================================
 * SPD院内耗材管理系统完整初始化脚本 V3.0
 * ==========================================
 * 功能说明:
 * 1. 数据库创建 - smart_admin_v3数据库及字符集配置
 * 2. Smart-Admin框架表 - 引用 smart_admin_v3.sql (自动导入)
 * 3. SPD业务表 - 6张核心业务表(物料/分类/单位/换算/供应商/厂家)
 * 4. 菜单配置 - 7个一级菜单 + 28个二级菜单
 * 5. 初始数据 - 单位/分类等基础数据
 * 
 * 技术规范:
 * - 框架: Spring Boot 3 + Vue 3 + MyBatis-Plus
 * - 字符集: UTF8MB4 (统一解决中文乱码)
 * - 主键: 雪花算法ID / 自增ID
 * - 删除: 逻辑删除(del_flag/deleted_flag)
 * - 多租户: tenant_id字段支持
 * 
 * 适用环境: MySQL 8.0+ | 引擎: InnoDB
 * 创建时间: 2025-12-28
 * 最后更新: 2025-12-28
 * 
 * 使用说明:
 * - 本脚本为完整的一键初始化脚本
 * - 执行前请备份现有数据(如有)
 * - 脚本会删除并重新创建 smart_admin_v3 数据库
 * - 建议在全新环境中执行,避免数据丢失
 * 
 * 执行方式(PowerShell):
 * Get-Content -Encoding UTF8 "SPD系统完整初始化脚本.sql" | mysql -h localhost -u root -proot
 * ==========================================
 */

-- ==========================================
-- 第一部分: 数据库创建
-- ==========================================

-- 删除已存在的数据库(警告: 会删除所有数据)
DROP DATABASE IF EXISTS `smart_admin_v3`;

-- 创建数据库并设置字符集
CREATE DATABASE IF NOT EXISTS `smart_admin_v3` 
    DEFAULT CHARACTER SET utf8mb4 
    DEFAULT COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE `smart_admin_v3`;

-- 设置连接字符集
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ==========================================
-- 第二部分: Smart-Admin框架基础表
-- ==========================================
-- 说明: 
-- 1. 框架表包含44个系统表(用户/角色/权限/部门/菜单/字典等)
-- 2. 框架SQL文件大小: ~177KB, 包含完整的表结构和初始数据
-- 3. 为避免脚本过大,框架表通过source命令引用
-- 4. 文件位置: smart_admin_v3.sql (与本脚本同目录)
-- ==========================================

-- 导入Smart-Admin框架基础表
SELECT '正在导入Smart-Admin框架表...' AS '提示';
SOURCE smart_admin_v3.sql;
SELECT 'Smart-Admin框架表导入完成!' AS '结果';

-- ==========================================
-- 第三部分: 数据库字符集优化
-- ==========================================
ALTER DATABASE smart_admin_v3 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- ==========================================
-- 第四部分: SPD业务表创建
-- ==========================================

-- ----------------------------
-- 1. 基础资料模块 (6张表)
-- ----------------------------

-- 1.1 耗材基础表
DROP TABLE IF EXISTS spd_material;
CREATE TABLE spd_material (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    material_id VARCHAR(32) NOT NULL COMMENT '耗材业务ID(MAT+年月日+6位)',
    material_name VARCHAR(128) NOT NULL COMMENT '耗材名称',
    material_code VARCHAR(64) COMMENT '耗材编码',
    specification VARCHAR(128) COMMENT '规格',
    model VARCHAR(64) COMMENT '型号',
    unit VARCHAR(16) COMMENT '基础单位',
    manufacturer VARCHAR(128) COMMENT '生产厂家',
    supplier_name VARCHAR(128) COMMENT '供应商',
    unit_price DECIMAL(18,2) COMMENT '单价',
    
    -- 集采相关
    is_collection_purchase TINYINT DEFAULT 0 COMMENT '是否集采(0=否/1=是)',
    collection_type VARCHAR(32) COMMENT '集采类型',
    
    -- 资质相关
    udi_code VARCHAR(64) COMMENT 'UDI编码',
    registration_no VARCHAR(64) COMMENT '注册证号',
    registration_valid_date DATE COMMENT '注册证有效期',
    is_high_value TINYINT DEFAULT 0 COMMENT '是否高值(0=否/1=是)',
    
    -- 状态字段
    material_status TINYINT DEFAULT 1 COMMENT '状态(1=可用/2=业务停用/3=质量停用)',
    
    -- 框架通用字段
    tenant_id VARCHAR(32) DEFAULT 'default' COMMENT '租户ID',
    del_flag TINYINT DEFAULT 0 COMMENT '删除标记(0=正常/1=已删除)',
    create_by VARCHAR(32) COMMENT '创建人',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(32) COMMENT '更新人',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    PRIMARY KEY (id),
    UNIQUE KEY uk_material_id (material_id, del_flag),
    KEY idx_name (material_name),
    KEY idx_collection (is_collection_purchase),
    KEY idx_status (material_status, del_flag)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='耗材基础表';

-- 1.2 耗材分类表
DROP TABLE IF EXISTS spd_material_category;
CREATE TABLE spd_material_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    category_code VARCHAR(50) COMMENT '分类编码',
    category_name VARCHAR(100) NOT NULL COMMENT '分类名称',
    parent_id BIGINT DEFAULT 0 COMMENT '父分类ID(0=顶级分类)',
    level INT DEFAULT 1 COMMENT '层级(1=一级/2=二级/3=三级)',
    sort INT DEFAULT 0 COMMENT '排序号',
    status TINYINT DEFAULT 1 COMMENT '状态(1=启用/0=停用)',
    remark VARCHAR(500) COMMENT '备注说明',
    
    deleted_flag TINYINT DEFAULT 0 COMMENT '删除标记(0=正常/1=已删除)',
    create_user_id BIGINT NOT NULL COMMENT '创建人ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_user_id BIGINT COMMENT '更新人ID',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    KEY idx_parent (parent_id),
    KEY idx_code (category_code),
    KEY idx_status (status),
    KEY idx_deleted (deleted_flag)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='耗材分类表';

-- 1.3 单位维护表
DROP TABLE IF EXISTS spd_unit;
CREATE TABLE spd_unit (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    unit_code VARCHAR(50) COMMENT '单位编码',
    unit_name VARCHAR(100) NOT NULL COMMENT '单位名称',
    unit_abbr VARCHAR(50) COMMENT '单位简称',
    unit_type TINYINT DEFAULT 1 COMMENT '单位类型(1=基本单位/2=辅助单位)',
    is_default TINYINT DEFAULT 0 COMMENT '是否默认(1=是/0=否)',
    sort INT DEFAULT 0 COMMENT '排序号',
    status TINYINT DEFAULT 1 COMMENT '状态(1=启用/0=停用)',
    remark VARCHAR(500) COMMENT '备注说明',
    
    deleted_flag TINYINT DEFAULT 0 COMMENT '删除标记(0=正常/1=已删除)',
    create_user_id BIGINT NOT NULL COMMENT '创建人ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_user_id BIGINT COMMENT '更新人ID',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    UNIQUE KEY uk_code (unit_code),
    KEY idx_name (unit_name),
    KEY idx_type (unit_type),
    KEY idx_status (status),
    KEY idx_deleted (deleted_flag)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='单位维护表';

-- 1.4 单位换算关系表
DROP TABLE IF EXISTS spd_unit_conversion;
CREATE TABLE spd_unit_conversion (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    from_unit_id BIGINT NOT NULL COMMENT '源单位ID',
    from_unit_name VARCHAR(100) COMMENT '源单位名称',
    to_unit_id BIGINT NOT NULL COMMENT '目标单位ID',
    to_unit_name VARCHAR(100) COMMENT '目标单位名称',
    conversion_rate DECIMAL(18,6) NOT NULL COMMENT '换算比率(1源单位=N目标单位)',
    is_reciprocal TINYINT DEFAULT 1 COMMENT '是否双向换算(1=是/0=否)',
    status TINYINT DEFAULT 1 COMMENT '状态(1=启用/0=停用)',
    remark VARCHAR(500) COMMENT '备注说明',
    
    deleted_flag TINYINT DEFAULT 0 COMMENT '删除标记(0=正常/1=已删除)',
    create_user_id BIGINT NOT NULL COMMENT '创建人ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_user_id BIGINT COMMENT '更新人ID',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    KEY idx_from_unit (from_unit_id),
    KEY idx_to_unit (to_unit_id),
    KEY idx_status (status),
    KEY idx_deleted (deleted_flag)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='单位换算关系表';

-- 1.5 供应商管理表
DROP TABLE IF EXISTS spd_supplier;
CREATE TABLE spd_supplier (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    supplier_code VARCHAR(50) COMMENT '供应商编码',
    supplier_name VARCHAR(200) NOT NULL COMMENT '供应商名称',
    contact_person VARCHAR(50) COMMENT '联系人',
    contact_phone VARCHAR(20) COMMENT '联系电话',
    address VARCHAR(500) COMMENT '地址',
    status TINYINT DEFAULT 1 COMMENT '状态(1=启用/0=停用)',
    
    deleted_flag TINYINT DEFAULT 0 COMMENT '删除标记',
    create_user_id BIGINT NOT NULL COMMENT '创建人ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_user_id BIGINT COMMENT '更新人ID',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    KEY idx_name (supplier_name),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='供应商管理表';

-- 1.6 生产厂家管理表
DROP TABLE IF EXISTS spd_manufacturer;
CREATE TABLE spd_manufacturer (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    manufacturer_code VARCHAR(50) COMMENT '厂家编码',
    manufacturer_name VARCHAR(200) NOT NULL COMMENT '厂家名称',
    contact_person VARCHAR(50) COMMENT '联系人',
    contact_phone VARCHAR(20) COMMENT '联系电话',
    address VARCHAR(500) COMMENT '地址',
    status TINYINT DEFAULT 1 COMMENT '状态(1=启用/0=停用)',
    
    deleted_flag TINYINT DEFAULT 0 COMMENT '删除标记',
    create_user_id BIGINT NOT NULL COMMENT '创建人ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_user_id BIGINT COMMENT '更新人ID',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    KEY idx_name (manufacturer_name),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='生产厂家管理表';

-- ==========================================
-- 第五部分: 初始化基础数据
-- ==========================================

-- 插入初始分类数据
INSERT INTO spd_material_category (id, category_code, category_name, parent_id, level, sort, create_user_id) VALUES
(1, 'HC', '耗材', 0, 1, 1, 1),
(2, 'HC_GZ', '骨科耗材', 1, 2, 1, 1),
(3, 'HC_XN', '心内耗材', 1, 2, 2, 1),
(4, 'HC_SJ', '神经外科耗材', 1, 2, 3, 1),
(5, 'HC_PT', '普通耗材', 1, 2, 4, 1),
(6, 'YL', '药品', 0, 1, 2, 1),
(7, 'SB', '设备', 0, 1, 3, 1);

-- 插入初始单位数据
INSERT INTO spd_unit (id, unit_code, unit_name, unit_abbr, unit_type, is_default, sort, create_user_id) VALUES
-- 基本单位
(1, 'PCS', '个', '个', 1, 1, 1, 1),
(2, 'BOX', '盒', '盒', 1, 0, 2, 1),
(3, 'BAG', '袋', '袋', 1, 0, 3, 1),
(4, 'BOTTLE', '瓶', '瓶', 1, 0, 4, 1),
(5, 'TUBE', '支', '支', 1, 0, 5, 1),
(6, 'SHEET', '片', '片', 1, 0, 6, 1),
(7, 'STRIP', '条', '条', 1, 0, 7, 1),
(8, 'SET', '套', '套', 1, 0, 8, 1),
-- 重量单位
(10, 'KG', '千克', 'kg', 2, 0, 10, 1),
(11, 'G', '克', 'g', 2, 0, 11, 1),
(12, 'MG', '毫克', 'mg', 2, 0, 12, 1),
-- 体积单位
(20, 'L', '升', 'L', 2, 0, 20, 1),
(21, 'ML', '毫升', 'ml', 2, 0, 21, 1),
-- 长度单位
(30, 'M', '米', 'm', 2, 0, 30, 1),
(31, 'CM', '厘米', 'cm', 2, 0, 31, 1);

-- 插入常用换算关系
INSERT INTO spd_unit_conversion (from_unit_id, from_unit_name, to_unit_id, to_unit_name, conversion_rate, create_user_id) VALUES
(2, '盒', 1, '个', 10, 1),
(2, '盒', 5, '支', 10, 1),
(10, '千克', 11, '克', 1000, 1),
(11, '克', 12, '毫克', 1000, 1),
(20, '升', 21, '毫升', 1000, 1),
(30, '米', 31, '厘米', 100, 1);

-- ==========================================
-- 第六部分: SPD菜单配置
-- ==========================================

-- 清理旧菜单
DELETE FROM t_menu WHERE menu_id >= 2000 AND menu_id < 3000;
DELETE FROM t_role_menu WHERE menu_id >= 2000 AND menu_id < 3000;

-- 创建SPD一级菜单
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, visible_flag, deleted_flag, create_user_id, create_time)
VALUES (2000, 'SPD耗材管理', 1, 0, 1, '/spd', 1, 0, 1, NOW());

-- 采购管理模块 (2100-2199)
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, web_perms, visible_flag, deleted_flag, create_user_id, create_time) VALUES
(2100, '采购管理', 1, 2000, 1, '/spd/procurement', NULL, NULL, 1, 0, 1, NOW()),
(2101, '申领单管理', 2, 2100, 1, 'apply', '/spd/apply/apply-list.vue', 'spd:apply:query', 1, 0, 1, NOW()),
(2102, '采购单管理', 2, 2100, 2, 'purchase', '/spd/purchase/purchase-list.vue', 'spd:purchase:query', 1, 0, 1, NOW()),
(2103, '收货单管理', 2, 2100, 3, 'receive', '/spd/receive/receive-list.vue', 'spd:receive:query', 1, 0, 1, NOW()),
(2104, '验收单管理', 2, 2100, 4, 'acceptance', '/spd/acceptance/acceptance-list.vue', 'spd:acceptance:query', 1, 0, 1, NOW());

-- 库存管理模块 (2200-2299)
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, web_perms, visible_flag, deleted_flag, create_user_id, create_time) VALUES
(2200, '库存管理', 1, 2000, 2, '/spd/inventory', NULL, NULL, 1, 0, 1, NOW()),
(2201, '打包单管理', 2, 2200, 1, 'package', '/spd/package/package-list.vue', 'spd:package:query', 1, 0, 1, NOW()),
(2202, '标签管理', 2, 2200, 2, 'label', '/spd/label/label-list.vue', 'spd:label:query', 1, 0, 1, NOW()),
(2203, '散货管理', 2, 2200, 3, 'bulk-material', '/spd/bulk-material/bulk-material-list.vue', 'spd:bulk:query', 1, 0, 1, NOW()),
(2204, '散货打包管理', 2, 2200, 4, 'bulk-package', '/spd/bulk-package/bulk-package-list.vue', 'spd:bulkpackage:query', 1, 0, 1, NOW()),
(2205, '定数包拆包管理', 2, 2200, 5, 'package-unpack', '/spd/package-unpack/package-unpack-list.vue', 'spd:unpack:query', 1, 0, 1, NOW()),
(2206, '库存安全量维护', 2, 2200, 6, 'stock-limit', '/spd/stock/stock-limit-list.vue', 'spd:stock:query', 1, 0, 1, NOW()),
(2207, '库存效期监控', 2, 2200, 7, 'expiry-monitor', '/spd/expiry-monitor/expiry-monitor.vue', 'spd:expiry:query', 1, 0, 1, NOW()),
(2208, '货位管理', 2, 2200, 8, 'location', '/spd/location/location-list.vue', 'spd:location:query', 1, 0, 1, NOW());

-- 科室管理模块 (2300-2399)
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, web_perms, visible_flag, deleted_flag, create_user_id, create_time) VALUES
(2300, '科室管理', 1, 2000, 3, '/spd/department', NULL, NULL, 1, 0, 1, NOW()),
(2301, '科室调拨管理', 2, 2300, 1, 'allocation', '/spd/allocation/allocation-list.vue', 'spd:allocation:query', 1, 0, 1, NOW()),
(2302, '科室退库管理', 2, 2300, 2, 'dept-return', '/spd/dept-return/return-list.vue', 'spd:deptreturn:query', 1, 0, 1, NOW()),
(2303, '科室收货管理', 2, 2300, 3, 'dept-receive', '/spd/dept-receive/dept-receive-list.vue', 'spd:deptreceive:query', 1, 0, 1, NOW()),
(2304, '科室消耗管理', 2, 2300, 4, 'dept-consume', '/spd/dept-consume/dept-consume-list.vue', 'spd:consume:query', 1, 0, 1, NOW()),
(2305, '科室分类管理', 2, 2300, 5, 'dept-category', '/spd/dept-category/dept-category-list.vue', 'spd:deptcategory:query', 1, 0, 1, NOW());

-- 退货管理模块 (2400-2499)
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, web_perms, visible_flag, deleted_flag, create_user_id, create_time) VALUES
(2400, '退货管理', 1, 2000, 4, '/spd/return', NULL, NULL, 1, 0, 1, NOW()),
(2401, '退供应商管理', 2, 2400, 1, 'supplier-return', '/spd/supplier-return/supplier-return-list.vue', 'spd:supplierreturn:query', 1, 0, 1, NOW());

-- 基础资料模块 (2500-2599)
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, web_perms, visible_flag, deleted_flag, create_user_id, create_time) VALUES
(2500, '基础资料', 1, 2000, 5, '/spd/basic-data', NULL, NULL, 1, 0, 1, NOW()),
(2501, '供应商管理', 2, 2500, 5, 'supplier', '/spd/supplier/supplier-list.vue', 'spd:supplier:query', 1, 0, 1, NOW()),
(2502, '生产厂家管理', 2, 2500, 6, 'manufacturer', '/spd/manufacturer/manufacturer-list.vue', 'spd:manufacturer:query', 1, 0, 1, NOW()),
(2503, '基础资料审核', 2, 2500, 7, 'data-audit', '/spd/audit/data-audit-list.vue', 'spd:audit:query', 1, 0, 1, NOW()),
(2504, '仓库人员对应关系', 2, 2500, 8, 'warehouse-user', '/spd/warehouse-user/warehouse-user-list.vue', 'spd:warehouseuser:query', 1, 0, 1, NOW()),
-- 新增模块
(2508, '物料管理', 2, 2500, 1, 'material', '/spd/material/material-list.vue', 'spd:material:query', 1, 0, 1, NOW()),
(2509, '耗材分类管理', 2, 2500, 2, 'category', '/spd/category/category-list.vue', 'spd:category:query', 1, 0, 1, NOW()),
(2510, '单位管理', 2, 2500, 3, 'unit', '/spd/unit/unit-list.vue', 'spd:unit:query', 1, 0, 1, NOW()),
(2511, '单位换算关系', 2, 2500, 4, 'unit-conversion', '/spd/unit-conversion/unit-conversion-list.vue', 'spd:unit:conversion:query', 1, 0, 1, NOW());

-- 追溯与报表模块 (2600-2699)
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, web_perms, visible_flag, deleted_flag, create_user_id, create_time) VALUES
(2600, '追溯与报表', 1, 2000, 6, '/spd/track-report', NULL, NULL, 1, 0, 1, NOW()),
(2601, '标签追溯管理', 2, 2600, 1, 'label-track', '/spd/label-track/label-track.vue', 'spd:track:query', 1, 0, 1, NOW()),
(2602, '库存统计报表', 2, 2600, 2, 'inventory-report', '/spd/inventory-report/inventory-report.vue', 'spd:report:query', 1, 0, 1, NOW());

-- 为超级管理员角色授权
INSERT INTO t_role_menu (role_id, menu_id, create_time, update_time)
SELECT 1, menu_id, NOW(), NOW()
FROM t_menu
WHERE menu_id >= 2000 AND menu_id < 3000
ON DUPLICATE KEY UPDATE update_time = NOW();

COMMIT;
SET FOREIGN_KEY_CHECKS = 1;

-- ==========================================
-- 脚本执行完成
-- ==========================================
SELECT '========== SPD系统初始化完成 ==========' AS '提示';
SELECT COUNT(*) AS '数据表总数' FROM information_schema.tables 
WHERE table_schema = 'smart_admin_v3' AND table_name LIKE 'spd_%';
SELECT COUNT(*) AS '菜单总数' FROM t_menu WHERE menu_id >= 2000 AND menu_id < 3000;
SELECT '单位初始数据:' AS info, COUNT(*) AS count FROM spd_unit;
SELECT '分类初始数据:' AS info, COUNT(*) AS count FROM spd_material_category;
SELECT '换算关系数据:' AS info, COUNT(*) AS count FROM spd_unit_conversion;
SELECT 'SPD系统初始化成功! 请重新登录系统查看菜单' AS '结果';

