package net.lab1024.sa.admin.module.spd.spdpackage.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;

@Data
@Schema(description = "打包单查询表单")
public class SpdPackageQueryForm extends PageParam {
    @Schema(description = "打包单号")
    private String packageCode;
    
    @Schema(description = "仓库ID")
    private String warehouseId;
    
    @Schema(description = "打包状态")
    private Integer packageStatus;
}
