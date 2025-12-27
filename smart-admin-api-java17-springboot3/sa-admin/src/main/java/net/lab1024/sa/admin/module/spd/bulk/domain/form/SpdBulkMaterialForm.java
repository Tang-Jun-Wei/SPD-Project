package net.lab1024.sa.admin.module.spd.bulk.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@Schema(description = "散货表单")
public class SpdBulkMaterialForm {
    @Schema(description = "主键ID")
    private Long id;
    
    @Schema(description = "耗材ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "耗材ID不能为空")
    private String materialId;
    
    @Schema(description = "批号ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "批号ID不能为空")
    private String batchId;
    
    @Schema(description = "入库总数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "入库总数量不能为空")
    private Integer totalNum;
    
    @Schema(description = "仓库ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "仓库不能为空")
    private String warehouseId;
}
