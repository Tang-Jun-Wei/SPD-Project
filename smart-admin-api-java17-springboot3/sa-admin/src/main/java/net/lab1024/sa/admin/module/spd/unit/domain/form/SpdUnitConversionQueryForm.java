package net.lab1024.sa.admin.module.spd.unit.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;

@Data
@Schema(description = "单位换算关系查询表单")
public class SpdUnitConversionQueryForm extends PageParam {
    @Schema(description = "源单位ID")
    private Long fromUnitId;
    
    @Schema(description = "目标单位ID")
    private Long toUnitId;
    
    @Schema(description = "状态")
    private Integer status;
}
