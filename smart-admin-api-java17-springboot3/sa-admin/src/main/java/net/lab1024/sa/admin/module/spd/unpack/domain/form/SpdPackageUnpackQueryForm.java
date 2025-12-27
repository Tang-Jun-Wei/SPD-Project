package net.lab1024.sa.admin.module.spd.unpack.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;

@Data
@Schema(description = "定数包拆包查询表单")
public class SpdPackageUnpackQueryForm extends PageParam {
    @Schema(description = "拆包单号")
    private String unpackNo;
    @Schema(description = "标签编码")
    private String labelCode;
    @Schema(description = "状态")
    private Integer status;
}
