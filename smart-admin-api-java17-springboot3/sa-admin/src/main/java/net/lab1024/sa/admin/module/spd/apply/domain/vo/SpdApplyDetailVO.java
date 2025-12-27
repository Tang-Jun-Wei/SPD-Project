package net.lab1024.sa.admin.module.spd.apply.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Schema(description = "申领明细视图对象")
public class SpdApplyDetailVO {

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

    @Schema(description = "申领数量")
    private BigDecimal applyQuantity;

    @Schema(description = "审批数量")
    private BigDecimal approveQuantity;

    @Schema(description = "批号ID")
    private String batchId;

    @Schema(description = "批号")
    private String batchNo;
}
