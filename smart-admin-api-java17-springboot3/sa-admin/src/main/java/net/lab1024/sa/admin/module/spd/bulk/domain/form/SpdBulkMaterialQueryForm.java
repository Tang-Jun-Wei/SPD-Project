package net.lab1024.sa.admin.module.spd.bulk.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;

@Data
@Schema(description = "散货查询表单")
public class SpdBulkMaterialQueryForm extends PageParam {
    @Schema(description = "散货业务ID")
    private String bulkId;
    
    @Schema(description = "耗材ID")
    private String materialId;
    
    @Schema(description = "耗材名称")
    private String materialName;
    
    @Schema(description = "批号")
    private String batchNo;
    
    @Schema(description = "仓库ID")
    private String warehouseId;
}
