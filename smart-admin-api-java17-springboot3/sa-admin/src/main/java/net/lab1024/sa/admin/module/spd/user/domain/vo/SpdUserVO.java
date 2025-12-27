package net.lab1024.sa.admin.module.spd.user.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * SPD用户管理 - 视图对象
 * 
 * 功能说明：
 * 1. 用于返回给前端的用户信息展示
 * 2. 包含用户基本信息和关联数据
 * 3. 用于列表查询、详情查询等场景
 * 
 * 业务规则：
 * - 不返回敏感信息（如密码）
 * - 返回格式化后的角色名称
 * - 返回租户信息用于多租户场景
 * 
 * @since 2025-12-27
 */
@Data
@Schema(description = "SPD用户视图对象")
public class SpdUserVO {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "用户业务ID（USER+年月日+6位序号）", example = "USER202512270001")
    private String userId;

    @Schema(description = "用户姓名", example = "张三")
    private String userName;

    @Schema(description = "用户手机号", example = "13800138000")
    private String userPhone;

    @Schema(description = "用户角色（1=普通用户 2=审核员 3=管理员）", example = "1")
    private Integer userRole;

    @Schema(description = "用户角色名称", example = "普通用户")
    private String userRoleName;

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
