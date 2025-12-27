package net.lab1024.sa.admin.module.spd.user.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * SPD用户管理 - 实体类
 * 
 * 功能说明：
 * 1. 对应数据库表 spd_user
 * 2. 使用雪花算法生成主键ID
 * 3. 支持逻辑删除（del_flag）
 * 4. 支持多租户（tenant_id）
 * 5. 记录创建人、创建时间、更新人、更新时间
 * 
 * 业务规则：
 * - 用户业务ID（user_id）格式：USER+年月日+6位序号
 * - 手机号+删除状态 唯一
 * - 角色：1=普通用户 2=审核员 3=管理员
 * 
 * @author SPD Team
 * @since 2025-12-27
 */
@Data
@TableName("spd_user")
public class SpdUserEntity {

    /**
     * 主键ID（雪花算法生成）
     * Smart-Admin框架自动生成
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户业务ID
     * 格式：USER+年月日+6位序号
     * 示例：USER202512270001
     */
    private String userId;

    /**
     * 用户姓名
     * 必填字段
     */
    private String userName;

    /**
     * 用户手机号
     * 唯一约束（与del_flag组合）
     * 11位数字
     */
    private String userPhone;

    /**
     * 用户角色
     * 1=普通用户：仅能查看和操作自己科室的数据
     * 2=审核员：可审核申领单、采购单等业务
     * 3=管理员：拥有所有权限
     * 默认值：1
     */
    private Integer userRole;

    /**
     * 租户ID（多租户隔离）
     * 默认值：default
     * 用于支持多医院/机构场景
     */
    private String tenantId;

    /**
     * 逻辑删除标识
     * 0=正常 1=已删除
     * 使用MyBatis-Plus逻辑删除功能
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建人ID
     * 关联user_id字段
     * 记录谁创建了这条记录
     */
    private String createBy;

    /**
     * 创建时间
     * 数据库自动填充
     */
    private LocalDateTime createTime;

    /**
     * 更新人ID
     * 关联user_id字段
     * 记录最后修改人
     */
    private String updateBy;

    /**
     * 更新时间
     * 数据库自动更新
     */
    private LocalDateTime updateTime;
}
