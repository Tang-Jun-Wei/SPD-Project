package net.lab1024.sa.admin.module.spd.orgnode.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * SPD组织节点管理 - 新增/编辑表单
 * 
 * 功能说明：
 * 1. 用于接收前端提交的组织节点新增/编辑请求
 * 2. 包含数据校验规则（JSR303）
 * 3. 支持科室和仓库两种节点类型
 * 
 * 校验规则：
 * - 节点名称：必填
 * - 节点类型：必填，1或2
 * - 科室需填写上级仓库ID
 * - 仓库需填写仓库类型
 * 
 * @since 2025-12-27
 */
@Data
@Schema(description = "SPD组织节点新增/编辑表单")
public class SpdOrgNodeForm {

    @Schema(description = "节点ID（编辑时传入）")
    private Long id;

    @Schema(description = "节点名称", required = true, example = "内科")
    @NotBlank(message = "节点名称不能为空")
    private String nodeName;

    @Schema(description = "节点类型（1=科室 2=仓库）", required = true, example = "1")
    @NotNull(message = "节点类型不能为空")
    private Integer nodeType;

    @Schema(description = "上级节点ID（科室关联仓库）", example = "WH202512270001")
    private String parentNodeId;

    @Schema(description = "仓库类型（1=中心库 2=科室库 3=临时库）", example = "1")
    private Integer warehouseType;

    @Schema(description = "仓库状态（1=启用 0=禁用）", example = "1")
    private Integer warehouseStatus;

    @Schema(description = "科室状态（1=启用 0=禁用）", example = "1")
    private Integer deptStatus;

    @Schema(description = "租户ID", example = "default")
    private String tenantId;
}
