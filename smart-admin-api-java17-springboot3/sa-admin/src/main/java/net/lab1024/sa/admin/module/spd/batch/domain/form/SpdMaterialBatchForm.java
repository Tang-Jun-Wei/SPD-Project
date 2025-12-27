package net.lab1024.sa.admin.module.spd.batch.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
@Schema(description = "SPD批号表单")
public class SpdMaterialBatchForm {
    @Schema(description = "批号ID")
    private Long id;
    
    @Schema(description = "耗材ID", required = true)
    @NotBlank(message = "耗材ID不能为空")
    private String materialId;
    
    @Schema(description = "批号", required = true)
    @NotBlank(message = "批号不能为空")
    private String batchNo;
    
    @Schema(description = "效期", required = true)
    @NotNull(message = "效期不能为空")
    private LocalDate validDate;
    
    @Schema(description = "供应商ID", required = true)
    @NotBlank(message = "供应商ID不能为空")
    private String supplierId;
    
    @Schema(description = "供应商名称")
    private String supplierName;
}
