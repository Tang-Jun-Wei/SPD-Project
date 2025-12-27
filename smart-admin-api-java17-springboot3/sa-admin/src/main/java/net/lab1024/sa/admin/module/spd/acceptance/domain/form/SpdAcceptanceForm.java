package net.lab1024.sa.admin.module.spd.acceptance.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 验收管理表单
 */
@Data
@Schema(description = "验收管理表单")
public class SpdAcceptanceForm {
    
    @Schema(description = "主键ID（编辑时必填）")
    private Long id;
    
    @Schema(description = "验收业务ID（编辑时必填）")
    private String acceptanceId;
    
    @NotBlank(message = "采购ID不能为空")
    @Schema(description = "采购ID")
    private String purchaseId;
    
    @NotBlank(message = "仓库ID不能为空")
    @Schema(description = "仓库ID")
    private String warehouseId;
    
    @NotNull(message = "验收日期不能为空")
    @Schema(description = "验收日期")
    private LocalDateTime acceptanceDate;
    
    @NotBlank(message = "验收人不能为空")
    @Schema(description = "验收人")
    private String acceptanceBy;
    
    @Schema(description = "验收状态")
    private Integer acceptanceStatus;
    
    @Schema(description = "总金额")
    private BigDecimal totalAmount;
    
    @Schema(description = "备注")
    private String remark;
    
    @Schema(description = "验收明细列表")
    private List<SpdAcceptanceDetailForm> detailList;
}
