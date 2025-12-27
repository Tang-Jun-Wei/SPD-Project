package net.lab1024.sa.admin.module.spd.acceptance.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "验收单表单")
public class SpdAcceptanceForm {
    private Long id;
    @NotBlank(message = "采购单号不能为空")
    private String purchaseCode;
    @NotBlank(message = "仓库ID不能为空")
    private String warehouseId;
    private String remark;
}
