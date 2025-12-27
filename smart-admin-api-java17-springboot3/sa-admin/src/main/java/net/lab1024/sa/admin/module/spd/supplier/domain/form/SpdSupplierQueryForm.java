package net.lab1024.sa.admin.module.spd.supplier.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;

@Data
@Schema(description = "供应商查询表单")
public class SpdSupplierQueryForm extends PageParam {

    @Schema(description = "供应商名称")
    private String supplierName;

    @Schema(description = "联系人")
    private String contactPerson;

    @Schema(description = "状态:1-启用,0-禁用")
    private Integer status;
}
