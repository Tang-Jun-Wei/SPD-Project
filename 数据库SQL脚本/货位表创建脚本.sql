-- 货位维护表
USE smart_admin_v3;

DROP TABLE IF EXISTS spd_location;
CREATE TABLE IF NOT EXISTS spd_location (
    id BIGINT NOT NULL COMMENT '主键ID（雪花算法）',
    location_id VARCHAR(32) NOT NULL COMMENT '货位业务ID（LOC+年月日+6位）',
    location_code VARCHAR(32) NOT NULL COMMENT '货位编码（如:A-01-01）',
    location_name VARCHAR(64) NOT NULL COMMENT '货位名称',
    node_id VARCHAR(32) NOT NULL COMMENT '关联节点ID（仓库/科室）',
    location_level TINYINT NOT NULL DEFAULT 1 COMMENT '货位层级：1=区 2=架 3=层 4=位',
    location_type TINYINT COMMENT '货位类型：1=常温 2=阴凉 3=冷藏 4=冷冻',
    max_capacity INT COMMENT '最大容量（件数）',
    current_quantity INT NOT NULL DEFAULT 0 COMMENT '当前存量（件数）',
    location_status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1=启用 0=禁用',
    remark VARCHAR(255) COMMENT '备注',
    tenant_id VARCHAR(32) DEFAULT 'default' COMMENT '租户ID',
    del_flag TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0=正常 1=删除',
    create_by VARCHAR(32) COMMENT '创建人ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(32) COMMENT '更新人ID',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_location_code (location_code, node_id, del_flag) COMMENT '货位编码+节点+删除状态唯一',
    UNIQUE KEY uk_location_id (location_id, del_flag) COMMENT '业务ID+删除状态唯一',
    INDEX idx_node_id (node_id, del_flag) COMMENT '节点检索',
    INDEX idx_location_status (location_status, del_flag) COMMENT '状态检索'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='货位维护表（关联科室/仓库节点）';

-- 初始化测试数据
INSERT INTO spd_location VALUES 
(1690000000000001001, 'LOC202512270001', 'A-01-01', 'A区01架01层01位', 'WH202512270001', 4, 1, 100, 0, 1, '常温货位', 'default', 0, 'USER202512270001', NOW(), NULL, NOW()),
(1690000000000001002, 'LOC202512270002', 'A-01-02', 'A区01架01层02位', 'WH202512270001', 4, 1, 100, 0, 1, NULL, 'default', 0, 'USER202512270001', NOW(), NULL, NOW()),
(1690000000000001003, 'LOC202512270003', 'B-01-01', 'B区01架01层01位', 'WH202512270001', 4, 2, 100, 0, 1, '阴凉货位', 'default', 0, 'USER202512270001', NOW(), NULL, NOW());
