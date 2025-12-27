package net.lab1024.sa.admin.module.spd.inventory.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 库存概览VO
 */
@Data
@Schema(description = "库存概览")
public class InventoryOverviewVO {
    
    @Schema(description = "库存总值")
    private Double totalValue;
    
    @Schema(description = "库存总数量")
    private Integer totalQuantity;
    
    @Schema(description = "库存预警数量")
    private Integer warningCount;
    
    @Schema(description = "效期预警数量")
    private Integer expiryWarningCount;
}
