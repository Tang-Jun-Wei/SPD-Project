package net.lab1024.sa.admin.module.spd.receive.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

@Data
@Schema(description = "收货表单")
public class SpdReceiveForm {
    @Schema(description = "主键ID")
    private Long id;
    
    @Schema(description = "验收单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "验收单号不能为空")
    private String acceptanceCode;
    
    @Schema(description = "仓库ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "仓库不能为空")
    private String warehouseId;
    
    @Schema(description = "收货类型：1=定数包 2=散货", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer receiveType;
    
    @Schema(description = "备注")
    private String remark;
    
    @Schema(description = "收货明细", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "收货明细不能为空")
    private List<SpdReceiveDetailForm> detailList;
}
