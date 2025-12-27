-- ==========================================
-- SPD耗材管理系统菜单配置脚本
-- 说明：配置SPD系统的完整菜单结构
-- 执行顺序：在执行全流程.sql之后运行此脚本
-- ==========================================

USE smart_admin_v3;

-- 1. 先删除原有的SPD菜单（如果存在）
DELETE FROM t_menu WHERE menu_name LIKE 'SPD%' OR menu_name LIKE '%耗材%' OR menu_name LIKE '%申领%' OR menu_name LIKE '%采购%' OR menu_name LIKE '%收货%';

-- 2. 创建SPD一级菜单（耗材管理）
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, perms_type, api_perms, web_perms, icon, context_menu_id, deleted_flag, create_user_id, create_time, update_user_id, update_time)
VALUES (2000, 'SPD耗材管理', 1, 0, 1, '/spd', NULL, 0, NULL, 0, 1, 0, 1, NULL, NULL, 'MedicineBoxOutlined', NULL, 0, 1, NOW(), 1, NOW());

-- 3. 创建二级菜单 - 基础数据
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, perms_type, api_perms, web_perms, icon, context_menu_id, deleted_flag, create_user_id, create_time, update_user_id, update_time)
VALUES 
(2100, '组织节点', 2, 2000, 1, '/spd/org-node/org-node-list', '/spd/org-node/org-node-list.vue', 0, NULL, 1, 1, 0, 1, NULL, NULL, 'ClusterOutlined', NULL, 0, 1, NOW(), 1, NOW()),
(2110, '耗材管理', 2, 2000, 2, '/spd/material/material-list', '/spd/material/material-list.vue', 0, NULL, 1, 1, 0, 1, NULL, NULL, 'AppstoreOutlined', NULL, 0, 1, NOW(), 1, NOW()),
(2120, '批号管理', 2, 2000, 3, '/spd/batch/batch-list', '/spd/batch/batch-list.vue', 0, NULL, 1, 1, 0, 1, NULL, NULL, 'BarcodeOutlined', NULL, 0, 1, NOW(), 1, NOW()),
(2130, '供应商管理', 2, 2000, 4, '/spd/supplier/supplier-list', '/spd/supplier/supplier-list.vue', 0, NULL, 1, 1, 0, 1, NULL, NULL, 'ShopOutlined', NULL, 0, 1, NOW(), 1, NOW());

-- 4. 创建二级菜单 - 业务流程
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, perms_type, api_perms, web_perms, icon, context_menu_id, deleted_flag, create_user_id, create_time, update_user_id, update_time)
VALUES 
(2200, '申领管理', 2, 2000, 11, '/spd/apply/apply-list', '/spd/apply/apply-list.vue', 0, NULL, 1, 1, 0, 1, NULL, NULL, 'FileTextOutlined', NULL, 0, 1, NOW(), 1, NOW()),
(2210, '采购管理', 2, 2000, 12, '/spd/purchase/purchase-list', '/spd/purchase/purchase-list.vue', 0, NULL, 1, 1, 0, 1, NULL, NULL, 'ShoppingCartOutlined', NULL, 0, 1, NOW(), 1, NOW()),
(2220, '收货管理', 2, 2000, 13, '/spd/receive/receive-list', '/spd/receive/receive-list.vue', 0, NULL, 1, 1, 0, 1, NULL, NULL, 'InboxOutlined', NULL, 0, 1, NOW(), 1, NOW());

-- 5. 创建二级菜单 - 库存管理
INSERT INTO t_menu (menu_id, menu_name, menu_type, parent_id, sort, path, component, frame_flag, frame_url, cache_flag, visible_flag, disabled_flag, perms_type, api_perms, web_perms, icon, context_menu_id, deleted_flag, create_user_id, create_time, update_user_id, update_time)
VALUES 
(2300, '标签管理', 2, 2000, 21, '/spd/label/label-list', '/spd/label/label-list.vue', 0, NULL, 1, 1, 0, 1, NULL, NULL, 'TagsOutlined', NULL, 0, 1, NOW(), 1, NOW()),
(2310, '散货管理', 2, 2000, 22, '/spd/bulk-material/bulk-material-list', '/spd/bulk-material/bulk-material-list.vue', 0, NULL, 1, 1, 0, 1, NULL, NULL, 'GoldOutlined', NULL, 0, 1, NOW(), 1, NOW()),
(2320, '库存报表', 2, 2000, 23, '/spd/inventory-report/inventory-report', '/spd/inventory-report/inventory-report.vue', 0, NULL, 1, 1, 0, 1, NULL, NULL, 'PieChartOutlined', NULL, 0, 1, NOW(), 1, NOW());

-- 6. 为管理员角色分配所有SPD菜单权限
-- 假设管理员角色ID为1，如果不是请修改
INSERT INTO t_role_menu (role_id, menu_id, create_time, update_time)
SELECT 1, menu_id, NOW(), NOW()
FROM t_menu
WHERE menu_id >= 2000 AND menu_id < 3000
ON DUPLICATE KEY UPDATE update_time = NOW();

-- 7. 验证菜单创建结果
SELECT 
    menu_id,
    menu_name,
    menu_type,
    CASE menu_type 
        WHEN 1 THEN '目录'
        WHEN 2 THEN '菜单'
        WHEN 3 THEN '按钮'
    END AS menu_type_name,
    path,
    component,
    sort,
    visible_flag
FROM t_menu
WHERE menu_id >= 2000 AND menu_id < 3000
ORDER BY menu_id;

-- ==========================================
-- 执行完成后：
-- 1. 重新登录系统
-- 2. 应该能看到"SPD耗材管理"菜单
-- 3. 展开可以看到所有10个子菜单
-- ==========================================
