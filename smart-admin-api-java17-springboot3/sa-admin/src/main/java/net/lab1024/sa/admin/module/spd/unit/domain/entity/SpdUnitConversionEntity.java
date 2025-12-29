package net.lab1024.sa.admin.module.spd.unit.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 单位换算关系实体
 */
@Data
@TableName("spd_unit_conversion")
public class SpdUnitConversionEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long fromUnitId;
    private String fromUnitName;
    private Long toUnitId;
    private String toUnitName;
    private BigDecimal conversionRate;
    private Integer isReciprocal;
    private Integer status;
    private String remark;

    @TableLogic
    private Integer deletedFlag;
    private Long createUserId;
    private LocalDateTime createTime;
    private Long updateUserId;
    private LocalDateTime updateTime;
}
