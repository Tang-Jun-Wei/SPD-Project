package net.lab1024.sa.admin.module.spd.orgnode.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * SPD组织节点管理 - 实体类
 * 
 * 功能说明：
 * 1. 对应数据库表 spd_org_node（科室-仓库组织节点表）
 * 2. 支持两种节点类型：科室(node_type=1)、仓库(node_type=2)
 * 3. 科室可关联上级仓库（parent_node_id）
 * 4. 仓库分为：中心库、科室库、临时库
 * 5. 使用雪花算法生成主键ID
 * 
 * 业务规则：
 * - 节点业务ID格式：DEPT/WH+年月日+6位序号
 * - 节点名称+类型+删除状态 唯一
 * - 支持多租户隔离（tenant_id）
 * - 支持逻辑删除（del_flag）
 * 
 * @since 2025-12-27
 */
@Data
@TableName("spd_org_node")
public class SpdOrgNodeEntity {

    /**
     * 主键ID（雪花算法生成）
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 节点业务ID
     * 格式：DEPT+年月日+6位序号（科室）或 WH+年月日+6位序号（仓库）
     * 示例：DEPT202512270001、WH202512270001
     */
    private String nodeId;

    /**
     * 节点名称
     * 科室示例：内科、外科、急诊科
     * 仓库示例：中心库、内科库、临时库
     */
    private String nodeName;

    /**
     * 节点类型
     * 1=科室：临床科室节点
     * 2=仓库：存储仓库节点
     */
    private Integer nodeType;

    /**
     * 上级节点ID（仅科室使用）
     * 关联科室所属的仓库node_id
     * 用于建立科室与仓库的关联关系
     */
    private String parentNodeId;

    /**
     * 仓库类型（仅仓库使用）
     * 1=中心库：医院总库房
     * 2=科室库：各科室二级库房
     * 3=临时库：临时存储库房
     */
    private Integer warehouseType;

    /**
     * 仓库状态（仅仓库使用）
     * 1=启用：仓库正常运行
     * 0=禁用：仓库暂停使用
     * 默认值：1
     */
    private Integer warehouseStatus;

    /**
     * 科室状态（仅科室使用）
     * 1=启用：科室正常运行
     * 0=禁用：科室暂停使用
     * 默认值：1
     */
    private Integer deptStatus;

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
     */
    private String updateBy;

    /**
     * 更新时间
     * 数据库自动更新
     */
    private LocalDateTime updateTime;
}
