package net.lab1024.sa.admin.module.spd.user.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;

/**
 * SPD用户管理 - 查询表单
 * 
 * 功能说明：
 * 1. 用于接收前端的分页查询请求
 * 2. 继承PageParam获取分页参数（pageNum、pageSize）
 * 3. 支持多条件组合查询
 * 
 * 查询条件：
 * - 用户姓名：支持模糊查询
 * - 手机号：支持精确查询
 * - 用户角色：支持精确查询
 * - 租户ID：支持精确查询
 * - 删除标识：默认查询未删除数据
 * 
 * @since 2025-12-27
 */
@Data
@Schema(description = "SPD用户查询表单")
public class SpdUserQueryForm extends PageParam {

    @Schema(description = "用户姓名（模糊查询）", example = "张三")
    private String userName;

    @Schema(description = "用户手机号（精确查询）", example = "13800138000")
    private String userPhone;

    @Schema(description = "用户角色（1=普通用户 2=审核员 3=管理员）", example = "1")
    private Integer userRole;

    @Schema(description = "租户ID", example = "default")
    private String tenantId;

    @Schema(description = "删除标识（0=正常 1=已删除）", example = "0")
    private Integer delFlag;
}
