package net.lab1024.sa.admin.module.spd.supplierreturn.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "退供应商表单")
public class SpdSupplierReturnForm {
    @Schema(description = "退供应商单ID(编辑时必填)")
    private String supplierReturnId;

    @Schema(description = "退货仓库节点ID", required = true)
    @NotBlank(message = "退货仓库不能为空")
    private String warehouseNodeId;

    @Schema(description = "供应商ID", required = true)
    @NotBlank(message = "供应商不能为空")
    private String supplierId;

    @Schema(description = "退货类型：1=质量问题 2=过期退货 3=订单错误 4=其他", required = true)
    @NotNull(message = "退货类型不能为空")
    private Integer returnType;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "明细列表", required = true)
    @NotNull(message = "明细列表不能为空")
    private List<DetailItem> detailList;

    @Data
    @Schema(description = "退货明细项")
    public static class DetailItem {
        @Schema(description = "耗材ID", required = true)
        @NotBlank(message = "耗材不能为空")
        private String materialId;

        @Schema(description = "批号ID", required = true)
        @NotBlank(message = "批号不能为空")
        private String batchId;

        @Schema(description = "退货数量", required = true)
        @NotNull(message = "退货数量不能为空")
        private Integer returnQuantity;

        @Schema(description = "退货原因")
        private String returnReason;
    }
}
