package net.lab1024.sa.admin.module.spd.orgnode.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;

/**
 * SPD组织节点管理 - 查询表单
 * 
 * 功能说明：
 * 1. 用于接收前端的分页查询请求
 * 2. 继承PageParam获取分页参数
 * 3. 支持多条件组合查询
 * 
 * 查询条件：
 * - 节点名称：支持模糊查询
 * - 节点类型：支持精确查询
 * - 上级节点：支持精确查询
 * - 租户ID：支持精确查询
 * 
 * @since 2025-12-27
 */
@Data
@Schema(description = "SPD组织节点查询表单")
public class SpdOrgNodeQueryForm extends PageParam {

    @Schema(description = "节点名称（模糊查询）", example = "内科")
    private String nodeName;

    @Schema(description = "节点类型（1=科室 2=仓库）", example = "1")
    private Integer nodeType;

    @Schema(description = "上级节点ID", example = "WH202512270001")
    private String parentNodeId;

    @Schema(description = "租户ID", example = "default")
    private String tenantId;

    @Schema(description = "删除标识（0=正常 1=已删除）", example = "0")
    private Integer delFlag;
}
