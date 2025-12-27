package net.lab1024.sa.admin.module.spd.apply.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Schema(description = "申领明细表单")
public class SpdApplyDetailForm {

    @Schema(description = "耗材ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "耗材ID不能为空")
    private String materialId;

    @Schema(description = "耗材名称")
    private String materialName;

    @Schema(description = "规格型号")
    private String specification;

    @Schema(description = "单位")
    private String unit;

    @Schema(description = "申领数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "申领数量不能为空")
    private BigDecimal applyQuantity;
}
