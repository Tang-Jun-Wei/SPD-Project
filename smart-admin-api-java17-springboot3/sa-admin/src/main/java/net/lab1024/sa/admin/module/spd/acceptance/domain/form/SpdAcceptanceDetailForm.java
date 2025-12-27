package net.lab1024.sa.admin.module.spd.acceptance.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

/**
 * 验收明细表单
 */
@Data
@Schema(description = "验收明细表单")
public class SpdAcceptanceDetailForm {
    
    @Schema(description = "主键ID")
    private Long id;
    
    @NotBlank(message = "耗材ID不能为空")
    @Schema(description = "耗材ID")
    private String materialId;
    
    @NotBlank(message = "批号ID不能为空")
    @Schema(description = "批号ID")
    private String batchId;
    
    @NotNull(message = "验收数量不能为空")
    @Schema(description = "验收数量")
    private BigDecimal acceptanceQuantity;
    
    @Schema(description = "合格数量")
    private BigDecimal qualifiedQuantity;
    
    @Schema(description = "不合格数量")
    private BigDecimal unqualifiedQuantity;
    
    @NotNull(message = "单价不能为空")
    @Schema(description = "单价")
    private BigDecimal unitPrice;
    
    @Schema(description = "总价")
    private BigDecimal totalPrice;
    
    @Schema(description = "质量状态")
    private String qualityStatus;
    
    @Schema(description = "备注")
    private String remark;
}
