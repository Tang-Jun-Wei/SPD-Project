package net.lab1024.sa.admin.module.spd.purchase.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "采购单视图")
public class SpdPurchaseVO {
    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "采购业务ID")
    private String purchaseId;
    @Schema(description = "采购单号")
    private String purchaseNo;
    @Schema(description = "供应商ID")
    private String supplierId;
    @Schema(description = "供应商名称")
    private String supplierName;
    @Schema(description = "仓库ID")
    private String warehouseId;
    @Schema(description = "仓库名称")
    private String warehouseName;
    @Schema(description = "总金额")
    private BigDecimal totalAmount;
    @Schema(description = "采购状态")
    private Integer purchaseStatus;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "备注")
    private String remark;
    @Schema(description = "采购明细")
    private List<SpdPurchaseDetailVO> detailList;
}
