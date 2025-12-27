package net.lab1024.sa.admin.module.spd.inventory.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 库存汇总VO
 */
@Data
@Schema(description = "库存汇总")
public class InventorySummaryVO {
    
    @Schema(description = "主键ID")
    private Long id;
    
    @Schema(description = "库存类型：label=定数包，bulk=散货")
    private String inventoryType;
    
    @Schema(description = "库存ID")
    private String inventoryId;
    
    @Schema(description = "耗材ID")
    private String materialId;
    
    @Schema(description = "耗材名称")
    private String materialName;
    
    @Schema(description = "规格型号")
    private String specification;
    
    @Schema(description = "单位")
    private String unit;
    
    @Schema(description = "批号ID")
    private String batchId;
    
    @Schema(description = "批号")
    private String batchNo;
    
    @Schema(description = "数量")
    private Integer quantity;
    
    @Schema(description = "已使用数量")
    private Integer usedNum;
    
    @Schema(description = "剩余数量")
    private Integer remainingNum;
    
    @Schema(description = "仓库ID")
    private String warehouseId;
    
    @Schema(description = "仓库名称")
    private String warehouseName;
    
    @Schema(description = "生产日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate productionDate;
    
    @Schema(description = "有效期至")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate validDate;
    
    @Schema(description = "距到期天数")
    private Integer daysToExpiry;
    
    @Schema(description = "状态")
    private Integer status;
    
    @Schema(description = "单价")
    private BigDecimal unitPrice;
    
    @Schema(description = "总价")
    private BigDecimal totalPrice;
}
