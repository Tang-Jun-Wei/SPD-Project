package net.lab1024.sa.admin.module.spd.unit.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Schema(description = "单位换算关系表单")
public class SpdUnitConversionForm {
    @Schema(description = "主键ID(修改时必填)")
    private Long id;
    
    @NotNull(message = "源单位ID不能为空")
    @Schema(description = "源单位ID")
    private Long fromUnitId;
    
    @NotNull(message = "目标单位ID不能为空")
    @Schema(description = "目标单位ID")
    private Long toUnitId;
    
    @NotNull(message = "换算比率不能为空")
    @Schema(description = "换算比率(1源单位=N目标单位)")
    private BigDecimal conversionRate;
    
    @Schema(description = "是否双向换算(1=是/0=否)")
    private Integer isReciprocal;
    
    @Schema(description = "状态(1=启用/0=停用)")
    private Integer status;
    
    @Schema(description = "备注")
    private String remark;
}
