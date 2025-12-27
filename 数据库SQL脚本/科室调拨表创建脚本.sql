-- 科室调拨模块（主表+明细表）
USE smart_admin_v3;

-- 调拨主表
DROP TABLE IF EXISTS spd_dept_allocation;
CREATE TABLE IF NOT EXISTS spd_dept_allocation (
    id BIGINT NOT NULL COMMENT '主键ID（雪花算法）',
    allocation_id VARCHAR(32) NOT NULL COMMENT '调拨单业务ID（ALLOC+年月日+6位）',
    allocation_code VARCHAR(32) NOT NULL COMMENT '调拨单号',
    from_node_id VARCHAR(32) NOT NULL COMMENT '调出节点ID',
    to_node_id VARCHAR(32) NOT NULL COMMENT '调入节点ID',
    allocation_status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1=待审核 2=已通过 3=已驳回 4=已完成',
    apply_user_id VARCHAR(32) NOT NULL COMMENT '申请人ID',
    apply_time DATETIME NOT NULL COMMENT '申请时间',
    audit_user_id VARCHAR(32) COMMENT '审核人ID',
    audit_time DATETIME COMMENT '审核时间',
    audit_opinion VARCHAR(255) COMMENT '审核意见',
    remark VARCHAR(255) COMMENT '备注',
    tenant_id VARCHAR(32) DEFAULT 'default' COMMENT '租户ID',
    del_flag TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0=正常 1=删除',
    create_by VARCHAR(32) COMMENT '创建人ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(32) COMMENT '更新人ID',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_allocation_code (allocation_code, del_flag),
    UNIQUE KEY uk_allocation_id (allocation_id, del_flag),
    INDEX idx_from_node (from_node_id, del_flag),
    INDEX idx_to_node (to_node_id, del_flag),
    INDEX idx_status (allocation_status, del_flag)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='科室调拨主表';

-- 调拨明细表
DROP TABLE IF EXISTS spd_dept_allocation_detail;
CREATE TABLE IF NOT EXISTS spd_dept_allocation_detail (
    id BIGINT NOT NULL COMMENT '主键ID（雪花算法）',
    allocation_id VARCHAR(32) NOT NULL COMMENT '关联调拨单ID',
    material_id VARCHAR(32) NOT NULL COMMENT '耗材ID',
    batch_id VARCHAR(32) NOT NULL COMMENT '批号ID',
    allocation_quantity INT NOT NULL COMMENT '调拨数量',
    actual_quantity INT COMMENT '实际调拨数量',
    remark VARCHAR(255) COMMENT '备注',
    tenant_id VARCHAR(32) DEFAULT 'default' COMMENT '租户ID',
    del_flag TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0=正常 1=删除',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    INDEX idx_allocation_id (allocation_id, del_flag),
    INDEX idx_material_batch (material_id, batch_id, del_flag)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='科室调拨明细表';

-- 初始化测试数据
INSERT INTO spd_dept_allocation VALUES 
(1690000000000002001, 'ALLOC202512270001', 'DB-20251227-001', 'DEPT202512270001', 'DEPT202512270002', 1, 'USER202512270001', NOW(), NULL, NULL, NULL, '内科调拨至外科', 'default', 0, 'USER202512270001', NOW(), NULL, NOW());

INSERT INTO spd_dept_allocation_detail VALUES 
(1690000000000002002, 'ALLOC202512270001', 'MAT202512270001', 'BATCH202512270001', 100, NULL, NULL, 'default', 0, NOW());
