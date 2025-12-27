package net.lab1024.sa.admin.module.spd.user.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * SPD用户管理 - 新增/编辑表单
 * 
 * 功能说明：
 * 1. 用于接收前端提交的用户新增/编辑请求
 * 2. 包含数据校验规则（JSR303）
 * 3. 提供Swagger接口文档注解
 * 
 * 校验规则：
 * - 用户姓名：必填
 * - 手机号：必填，11位数字格式
 * - 角色：必填，1-3范围
 * 
 * @author SPD Team
 * @since 2025-12-27
 */
@Data
@Schema(description = "SPD用户新增/编辑表单")
public class SpdUserForm {

    /**
     * 用户ID（编辑时必填，新增时为空）
     * 更新操作通过此字段判断
     */
    @Schema(description = "用户ID（编辑时传入）")
    private Long id;

    /**
     * 用户姓名
     * 必填字段，用于显示和检索
     */
    @Schema(description = "用户姓名", required = true, example = "张三")
    @NotBlank(message = "用户姓名不能为空")
    private String userName;

    /**
     * 用户手机号
     * 必填字段，用于登录和唯一标识
     * 格式：11位数字
     */
    @Schema(description = "用户手机号", required = true, example = "13800138000")
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String userPhone;

    /**
     * 用户角色
     * 必填字段
     * 1=普通用户 2=审核员 3=管理员
     */
    @Schema(description = "用户角色（1=普通用户 2=审核员 3=管理员）", required = true, example = "1")
    @NotNull(message = "用户角色不能为空")
    private Integer userRole;

    /**
     * 租户ID（多租户场景）
     * 可选字段，默认值由后端填充
     */
    @Schema(description = "租户ID", example = "default")
    private String tenantId;
}
