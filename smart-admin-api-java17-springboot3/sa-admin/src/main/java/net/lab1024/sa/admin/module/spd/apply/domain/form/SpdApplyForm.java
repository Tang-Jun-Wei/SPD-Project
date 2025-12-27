package net.lab1024.sa.admin.module.spd.apply.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

@Data
@Schema(description = "申领表单")
public class SpdApplyForm {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "申领科室ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "申领科室不能为空")
    private String applyDeptId;

    @Schema(description = "申领科室名称")
    private String applyDeptName;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "申领明细", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "申领明细不能为空")
    private List<SpdApplyDetailForm> detailList;
}
