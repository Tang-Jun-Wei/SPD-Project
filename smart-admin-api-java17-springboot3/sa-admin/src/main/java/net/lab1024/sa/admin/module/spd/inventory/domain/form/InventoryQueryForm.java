package net.lab1024.sa.admin.module.spd.inventory.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;

/**
 * 库存查询表单
 */
@Data
@Schema(description = "库存查询表单")
public class InventoryQueryForm extends PageParam {
    
    @Schema(description = "耗材名称")
    private String materialName;
    
    @Schema(description = "仓库ID")
    private String warehouseId;
    
    @Schema(description = "库存类型：label=定数包，bulk=散货")
    private String inventoryType;
}
