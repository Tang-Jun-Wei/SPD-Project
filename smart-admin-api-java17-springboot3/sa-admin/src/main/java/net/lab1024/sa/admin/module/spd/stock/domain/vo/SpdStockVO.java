package net.lab1024.sa.admin.module.spd.stock.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "库存视图")
public class SpdStockVO {
    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "库存业务ID")
    private String stockId;
    @Schema(description = "仓库ID")
    private String warehouseId;
    @Schema(description = "仓库名称")
    private String warehouseName;
    @Schema(description = "耗材ID")
    private String materialId;
    @Schema(description = "耗材名称")
    private String materialName;
    @Schema(description = "批号ID")
    private String batchId;
    @Schema(description = "批号")
    private String batchNo;
    @Schema(description = "库存数量")
    private BigDecimal quantity;
    @Schema(description = "锁定数量")
    private BigDecimal lockedQuantity;
    @Schema(description = "可用数量")
    private BigDecimal availableQuantity;
    @Schema(description = "有效期")
    private LocalDateTime expiryDate;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
