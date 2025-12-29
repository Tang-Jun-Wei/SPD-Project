package net.lab1024.sa.admin.module.spd.category.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 耗材分类实体
 * 
 * @author 1024创新实验室
 */
@Data
@TableName("spd_material_category")
public class SpdMaterialCategoryEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 分类编码
     */
    private String categoryCode;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 父分类ID(0=顶级分类)
     */
    private Long parentId;

    /**
     * 层级(1=一级/2=二级/3=三级)
     */
    private Integer level;

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 状态(1=启用/0=停用)
     */
    private Integer status;

    /**
     * 备注说明
     */
    private String remark;

    /**
     * 删除标记(0=正常/1=已删除)
     */
    @TableLogic
    private Integer deletedFlag;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新人ID
     */
    private Long updateUserId;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
