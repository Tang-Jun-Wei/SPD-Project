package net.lab1024.sa.admin.module.spd.unit.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;

@Data
@Schema(description = "单位查询表单")
public class SpdUnitQueryForm extends PageParam {
    @Schema(description = "单位名称")
    private String unitName;
    
    @Schema(description = "单位类型")
    private Integer unitType;
    
    @Schema(description = "状态")
    private Integer status;
}
