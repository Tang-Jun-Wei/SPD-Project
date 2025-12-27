package net.lab1024.sa.admin.module.spd.stock.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;

@Data
@Schema(description = "库存查询表单")
public class SpdStockQueryForm extends PageParam {
    @Schema(description = "仓库ID")
    private String warehouseId;
    @Schema(description = "耗材ID")
    private String materialId;
    @Schema(description = "耗材名称")
    private String materialName;
    @Schema(description = "批号")
    private String batchNo;
}
