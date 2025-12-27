package net.lab1024.sa.admin.module.spd.receive.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@Schema(description = "收货明细表单")
public class SpdReceiveDetailForm {
    @Schema(description = "耗材ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "耗材ID不能为空")
    private String materialId;
    
    @Schema(description = "批号ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "批号ID不能为空")
    private String batchId;
    
    @Schema(description = "收货数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "收货数量不能为空")
    private Integer receiveNum;
    
    @Schema(description = "包装规格")
    private String packageSpec;
    
    @Schema(description = "标签码")
    private String labelCode;
}
