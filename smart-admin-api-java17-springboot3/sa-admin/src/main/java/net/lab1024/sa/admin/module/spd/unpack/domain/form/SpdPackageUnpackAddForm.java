package net.lab1024.sa.admin.module.spd.unpack.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Schema(description = "定数包拆包新增表单")
public class SpdPackageUnpackAddForm {
    @Schema(description = "标签编码")
    @NotBlank(message = "标签编码不能为空")
    private String labelCode;
    
    @Schema(description = "耗材ID")
    private Long materialId;
    
    @Schema(description = "耗材名称")
    private String materialName;
    
    @Schema(description = "规格型号")
    private String specification;
    
    @Schema(description = "批号")
    private String batchNo;
    
    @Schema(description = "定数包数量")
    @NotNull(message = "定数包数量不能为空")
    private BigDecimal packageQuantity;
    
    @Schema(description = "转换散货数量")
    @NotNull(message = "转换散货数量不能为空")
    private BigDecimal bulkQuantity;
    
    @Schema(description = "仓库ID")
    private Long warehouseId;
    
    @Schema(description = "仓库名称")
    private String warehouseName;
    
    @Schema(description = "备注")
    private String remark;
}
