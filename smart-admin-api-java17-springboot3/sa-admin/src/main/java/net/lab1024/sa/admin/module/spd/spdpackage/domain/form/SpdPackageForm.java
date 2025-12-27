package net.lab1024.sa.admin.module.spd.spdpackage.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
@Schema(description = "打包单表单")
public class SpdPackageForm {
    @Schema(description = "主键ID（编辑时必填）")
    private Long id;
    
    @Schema(description = "打包单业务ID（编辑时必填）")
    private String packageId;
    
    @NotBlank(message = "仓库ID不能为空")
    @Schema(description = "仓库ID")
    private String warehouseId;
    
    @Schema(description = "打包状态")
    private Integer packageStatus;
    
    @Schema(description = "备注")
    private String remark;
}
