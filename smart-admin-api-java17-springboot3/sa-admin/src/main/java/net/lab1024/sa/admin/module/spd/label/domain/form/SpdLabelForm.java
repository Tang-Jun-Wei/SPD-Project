package net.lab1024.sa.admin.module.spd.label.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@Schema(description = "标签表单")
public class SpdLabelForm {
    @Schema(description = "主键ID")
    private Long id;
    
    @Schema(description = "标签码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "标签码不能为空")
    private String labelCode;
    
    @Schema(description = "耗材ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "耗材ID不能为空")
    private String materialId;
    
    @Schema(description = "批号ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "批号ID不能为空")
    private String batchId;
    
    @Schema(description = "包装规格", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "包装规格不能为空")
    private String packageSpec;
    
    @Schema(description = "单包数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "单包数量不能为空")
    private Integer packageNum;
    
    @Schema(description = "仓库ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "仓库不能为空")
    private String warehouseId;
}
