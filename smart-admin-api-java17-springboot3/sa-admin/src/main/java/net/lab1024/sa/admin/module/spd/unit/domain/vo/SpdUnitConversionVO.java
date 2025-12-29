package net.lab1024.sa.admin.module.spd.unit.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "单位换算关系VO")
public class SpdUnitConversionVO {
    @Schema(description = "主键ID")
    private Long id;
    
    @Schema(description = "源单位ID")
    private Long fromUnitId;
    
    @Schema(description = "源单位名称")
    private String fromUnitName;
    
    @Schema(description = "目标单位ID")
    private Long toUnitId;
    
    @Schema(description = "目标单位名称")
    private String toUnitName;
    
    @Schema(description = "换算比率")
    private BigDecimal conversionRate;
    
    @Schema(description = "是否双向换算(1=是/0=否)")
    private Integer isReciprocal;
    
    @Schema(description = "状态(1=启用/0=停用)")
    private Integer status;
    
    @Schema(description = "备注")
    private String remark;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
