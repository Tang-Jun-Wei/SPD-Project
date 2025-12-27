package net.lab1024.sa.admin.module.spd.supplier.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
@Schema(description = "供应商表单")
public class SpdSupplierForm {

    @Schema(description = "主键ID")
    private Long id;

    @NotBlank(message = "供应商名称不能为空")
    @Schema(description = "供应商名称")
    private String supplierName;

    @NotBlank(message = "联系人不能为空")
    @Schema(description = "联系人")
    private String contactPerson;

    @NotBlank(message = "联系电话不能为空")
    @Schema(description = "联系电话")
    private String contactPhone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "营业执照号")
    private String businessLicense;

    @Schema(description = "状态:1-启用,0-禁用")
    private Integer status;

    @Schema(description = "备注")
    private String remark;
}
