package net.lab1024.sa.admin.module.spd.stocklimit.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;

@Data
@Schema(description = "SPD库存安全量查询表单")
public class SpdStockLimitQueryForm extends PageParam {

    @Schema(description = "耗材名称")
    private String materialName;

    @Schema(description = "仓库ID")
    private String warehouseId;

    @Schema(description = "库存状态：0=正常 1=低于预警 2=低于下限 3=超过上限")
    private Integer stockStatus;
}
