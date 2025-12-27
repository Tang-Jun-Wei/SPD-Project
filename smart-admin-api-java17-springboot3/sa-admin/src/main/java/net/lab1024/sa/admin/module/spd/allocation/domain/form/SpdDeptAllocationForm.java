package net.lab1024.sa.admin.module.spd.allocation.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
@Schema(description = "科室调拨表单")
public class SpdDeptAllocationForm {
    @Schema(description = "调拨单ID(编辑时必填)")
    private String allocationId;

    @Schema(description = "调出节点ID", required = true)
    @NotBlank(message = "调出节点不能为空")
    private String fromNodeId;

    @Schema(description = "调入节点ID", required = true)
    @NotBlank(message = "调入节点不能为空")
    private String toNodeId;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "明细列表", required = true)
    @NotNull(message = "明细列表不能为空")
    private List<DetailItem> detailList;

    @Data
    @Schema(description = "调拨明细项")
    public static class DetailItem {
        @Schema(description = "耗材ID", required = true)
        @NotBlank(message = "耗材不能为空")
        private String materialId;

        @Schema(description = "批号ID", required = true)
        @NotBlank(message = "批号不能为空")
        private String batchId;

        @Schema(description = "调拨数量", required = true)
        @NotNull(message = "调拨数量不能为空")
        private Integer allocationQuantity;

        @Schema(description = "备注")
        private String remark;
    }
}
