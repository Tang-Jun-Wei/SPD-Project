-- 退供应商主表
DROP TABLE IF EXISTS spd_supplier_return;
CREATE TABLE IF NOT EXISTS spd_supplier_return (
    id BIGINT NOT NULL COMMENT '主键ID（雪花算法）',
    supplier_return_id VARCHAR(32) NOT NULL COMMENT '退供应商单业务ID（SUPRET+年月日+6位）',
    supplier_return_code VARCHAR(32) NOT NULL COMMENT '退供应商单号',
    warehouse_node_id VARCHAR(32) NOT NULL COMMENT '退货仓库节点ID',
    supplier_id VARCHAR(32) NOT NULL COMMENT '供应商ID',
    return_status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1=待审核 2=已通过 3=已驳回 4=已完成',
    return_type TINYINT NOT NULL COMMENT '退货类型：1=质量问题 2=过期退货 3=订单错误 4=其他',
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
    UNIQUE KEY uk_supplier_return_code (supplier_return_code, del_flag),
    UNIQUE KEY uk_supplier_return_id (supplier_return_id, del_flag),
    INDEX idx_warehouse_node (warehouse_node_id, del_flag),
    INDEX idx_supplier (supplier_id, del_flag),
    INDEX idx_status (return_status, del_flag),
    INDEX idx_type (return_type, del_flag)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='退供应商主表';

-- 退供应商明细表
DROP TABLE IF EXISTS spd_supplier_return_detail;
CREATE TABLE IF NOT EXISTS spd_supplier_return_detail (
    id BIGINT NOT NULL COMMENT '主键ID（雪花算法）',
    supplier_return_id VARCHAR(32) NOT NULL COMMENT '关联退供应商单ID',
    material_id VARCHAR(32) NOT NULL COMMENT '耗材ID',
    batch_id VARCHAR(32) NOT NULL COMMENT '批号ID',
    return_quantity INT NOT NULL COMMENT '退货数量',
    actual_quantity INT COMMENT '实际退货数量',
    return_reason VARCHAR(255) COMMENT '退货原因',
    remark VARCHAR(255) COMMENT '备注',
    tenant_id VARCHAR(32) DEFAULT 'default' COMMENT '租户ID',
    del_flag TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0=正常 1=删除',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    INDEX idx_supplier_return_id (supplier_return_id, del_flag),
    INDEX idx_material_batch (material_id, batch_id, del_flag)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='退供应商明细表';
