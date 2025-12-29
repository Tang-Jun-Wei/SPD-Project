package net.lab1024.sa.admin.module.spd.unit.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 单位实体
 */
@Data
@TableName("spd_unit")
public class SpdUnitEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String unitCode;
    private String unitName;
    private String unitAbbr;
    private Integer unitType;
    private Integer isDefault;
    private Integer sort;
    private Integer status;
    private String remark;

    @TableLogic
    private Integer deletedFlag;
    private Long createUserId;
    private LocalDateTime createTime;
    private Long updateUserId;
    private LocalDateTime updateTime;
}
