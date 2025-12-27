package net.lab1024.sa.admin.module.spd.purchase.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

@Data
@Schema(description = "采购表单")
public class SpdPurchaseForm {
    @Schema(description = "主键ID")
    private Long id;
    
    @Schema(description = "供应商ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "供应商不能为空")
    private String supplierId;
    
    @Schema(description = "供应商名称")
    private String supplierName;
    
    @Schema(description = "仓库ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "仓库不能为空")
    private String warehouseId;
    
    @Schema(description = "仓库名称")
    private String warehouseName;
    
    @Schema(description = "备注")
    private String remark;
    
    @Schema(description = "采购明细", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "采购明细不能为空")
    private List<SpdPurchaseDetailForm> detailList;
}
