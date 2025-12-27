package net.lab1024.sa.admin.module.spd.orgnode.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * SPD组织节点管理 - 视图对象
 * 
 * 功能说明：
 * 1. 用于返回给前端的组织节点信息展示
 * 2. 包含节点基本信息和关联数据
 * 3. 包含类型名称、状态名称的转换
 * 
 * 业务规则：
 * - 返回节点类型名称（科室/仓库）
 * - 返回仓库类型名称（中心库/科室库/临时库）
 * - 返回状态名称（启用/禁用）
 * 
 * @since 2025-12-27
 */
@Data
@Schema(description = "SPD组织节点视图对象")
public class SpdOrgNodeVO {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "节点业务ID", example = "DEPT202512270001")
    private String nodeId;

    @Schema(description = "节点名称", example = "内科")
    private String nodeName;

    @Schema(description = "节点类型（1=科室 2=仓库）", example = "1")
    private Integer nodeType;

    @Schema(description = "节点类型名称", example = "科室")
    private String nodeTypeName;

    @Schema(description = "上级节点ID", example = "WH202512270001")
    private String parentNodeId;

    @Schema(description = "上级节点名称", example = "中心库")
    private String parentNodeName;

    @Schema(description = "仓库类型（1=中心库 2=科室库 3=临时库）", example = "1")
    private Integer warehouseType;

    @Schema(description = "仓库类型名称", example = "中心库")
    private String warehouseTypeName;

    @Schema(description = "仓库状态（1=启用 0=禁用）", example = "1")
    private Integer warehouseStatus;

    @Schema(description = "仓库状态名称", example = "启用")
    private String warehouseStatusName;

    @Schema(description = "科室状态（1=启用 0=禁用）", example = "1")
    private Integer deptStatus;

    @Schema(description = "科室状态名称", example = "启用")
    private String deptStatusName;

    @Schema(description = "租户ID", example = "default")
    private String tenantId;

    @Schema(description = "逻辑删除标识（0=正常 1=已删除）", example = "0")
    private Integer delFlag;

    @Schema(description = "创建人ID")
    private String createBy;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新人ID")
    private String updateBy;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
