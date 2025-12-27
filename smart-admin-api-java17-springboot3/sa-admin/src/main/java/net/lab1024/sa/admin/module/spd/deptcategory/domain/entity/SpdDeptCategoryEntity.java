package net.lab1024.sa.admin.module.spd.deptcategory.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("spd_dept_category")
public class SpdDeptCategoryEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String categoryId;

    private String categoryName;

    private String categoryCode;

    private String parentId;

    private Integer categoryLevel;

    private Integer sortOrder;

    private Integer categoryStatus;

    private String remark;

    private String tenantId;

    @TableLogic
    private Integer delFlag;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;
}
