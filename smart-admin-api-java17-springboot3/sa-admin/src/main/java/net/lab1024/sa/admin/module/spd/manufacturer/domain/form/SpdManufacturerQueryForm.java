package net.lab1024.sa.admin.module.spd.manufacturer.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;

@Data
@Schema(description = "生产厂家查询表单")
public class SpdManufacturerQueryForm extends PageParam {

    @Schema(description = "厂家名称")
    private String manufacturerName;

    @Schema(description = "联系人")
    private String contactPerson;

    @Schema(description = "状态:1-启用,0-禁用")
    private Integer status;
}
