package net.lab1024.sa.admin.module.spd.batch.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 批号信息表单
 */
@Data
@Schema(description = "批号信息表单")
public class SpdBatchForm {

    @Schema(description = "主键ID（修改时必填）")
    private Long id;

    @Schema(description = "批号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "批号不能为空")
    private String batchNo;

    @Schema(description = "耗材ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "耗材ID不能为空")
    private String materialId;

    @Schema(description = "耗材名称")
    private String materialName;

    @Schema(description = "生产日期")
    private LocalDateTime productionDate;

    @Schema(description = "有效期至", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "有效期不能为空")
    private LocalDateTime expiryDate;

    @Schema(description = "灭菌日期")
    private LocalDateTime sterilizationDate;

    @Schema(description = "灭菌批号")
    private String sterilizationBatchNo;

    @Schema(description = "采购价格")
    private BigDecimal purchasePrice;

    @Schema(description = "供应商ID")
    private String supplierId;

    @Schema(description = "供应商名称")
    private String supplierName;

    @Schema(description = "批号状态（0-停用，1-启用）")
    private Integer batchStatus;

    @Schema(description = "备注")
    private String remark;
}
