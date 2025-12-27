package net.lab1024.sa.admin.module.spd.stocklimit.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@Schema(description = "SPD库存安全量表单")
public class SpdStockLimitForm {

    @Schema(description = "主键ID（修改时必填）")
    private Long id;

    @Schema(description = "耗材业务ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "耗材业务ID不能为空")
    private String materialId;

    @Schema(description = "仓库ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "仓库ID不能为空")
    private String warehouseId;

    @Schema(description = "最小库存量（安全下限）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "最小库存量不能为空")
    private Integer minStock;

    @Schema(description = "最大库存量（安全上限）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "最大库存量不能为空")
    private Integer maxStock;

    @Schema(description = "预警库存量")
    private Integer warningStock;

    @Schema(description = "备注")
    private String remark;
}
