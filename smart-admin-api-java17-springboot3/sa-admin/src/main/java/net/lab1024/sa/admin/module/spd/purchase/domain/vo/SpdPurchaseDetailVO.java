package net.lab1024.sa.admin.module.spd.purchase.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "采购明细视图")
public class SpdPurchaseDetailVO {
    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "耗材ID")
    private String materialId;
    @Schema(description = "耗材名称")
    private String materialName;
    @Schema(description = "规格型号")
    private String specification;
    @Schema(description = "单位")
    private String unit;
    @Schema(description = "数量")
    private BigDecimal quantity;
    @Schema(description = "单价")
    private BigDecimal unitPrice;
    @Schema(description = "总价")
    private BigDecimal totalPrice;
    @Schema(description = "批号")
    private String batchNo;
    @Schema(description = "有效期")
    private LocalDateTime expiryDate;
}
