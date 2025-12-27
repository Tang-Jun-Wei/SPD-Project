package net.lab1024.sa.admin.module.spd.material.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 耗材信息表单
 */
@Data
@Schema(description = "耗材信息表单")
public class SpdMaterialForm {

    @Schema(description = "主键ID（修改时必填）")
    private Long id;

    @Schema(description = "耗材名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "耗材名称不能为空")
    private String materialName;

    @Schema(description = "耗材编码")
    private String materialCode;

    @Schema(description = "规格型号")
    private String specification;

    @Schema(description = "单位", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "单位不能为空")
    private String unit;

    @Schema(description = "生产厂家", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "生产厂家不能为空")
    private String manufacturer;

    @Schema(description = "供应商ID")
    private String supplierId;

    @Schema(description = "供应商名称")
    private String supplierName;

    @Schema(description = "单价", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "单价不能为空")
    private BigDecimal unitPrice;

    @Schema(description = "是否集采（0-否，1-是）")
    private Integer isGroupBuy;

    @Schema(description = "UDI编码")
    private String udiCode;

    @Schema(description = "注册证号")
    private String registrationNo;

    @Schema(description = "注册证有效期")
    private LocalDateTime registrationExpiry;

    @Schema(description = "耗材分类")
    private String category;

    @Schema(description = "耗材状态（0-停用，1-启用）")
    private Integer materialStatus;

    @Schema(description = "备注")
    private String remark;
}
