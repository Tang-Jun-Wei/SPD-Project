package net.lab1024.sa.admin.module.spd.deptreturn.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "科室退库表单")
public class SpdDeptReturnForm {
    @Schema(description = "退库单ID(编辑时必填)")
    private String returnId;

    @Schema(description = "退库科室节点ID", required = true)
    @NotBlank(message = "退库科室不能为空")
    private String fromNodeId;

    @Schema(description = "接收仓库节点ID", required = true)
    @NotBlank(message = "接收仓库不能为空")
    private String toNodeId;

    @Schema(description = "退库类型：1=正常退库 2=过期退库 3=损坏退库 4=其他", required = true)
    @NotNull(message = "退库类型不能为空")
    private Integer returnType;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "明细列表", required = true)
    @NotNull(message = "明细列表不能为空")
    private List<DetailItem> detailList;

    @Data
    @Schema(description = "退库明细项")
    public static class DetailItem {
        @Schema(description = "耗材ID", required = true)
        @NotBlank(message = "耗材不能为空")
        private String materialId;

        @Schema(description = "批号ID", required = true)
        @NotBlank(message = "批号不能为空")
        private String batchId;

        @Schema(description = "退库数量", required = true)
        @NotNull(message = "退库数量不能为空")
        private Integer returnQuantity;

        @Schema(description = "退库原因")
        private String returnReason;
    }
}
