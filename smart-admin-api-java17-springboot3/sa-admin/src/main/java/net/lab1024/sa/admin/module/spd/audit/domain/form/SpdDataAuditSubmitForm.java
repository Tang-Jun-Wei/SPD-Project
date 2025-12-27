package net.lab1024.sa.admin.module.spd.audit.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@Schema(description = "SPD基础资料审核提交表单")
public class SpdDataAuditSubmitForm {

    @Schema(description = "审核类型：1=供应商 2=耗材 3=生产厂家 4=调价 5=收费状态", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "审核类型不能为空")
    private Integer auditType;

    @Schema(description = "关联目标业务ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "目标业务ID不能为空")
    private String targetId;

    @Schema(description = "目标名称")
    private String targetName;

    @Schema(description = "操作类型：1=新增 2=修改", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "操作类型不能为空")
    private Integer actionType;

    @Schema(description = "修改前数据（JSON）")
    private String oldData;

    @Schema(description = "新数据或修改后数据（JSON）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "新数据不能为空")
    private String newData;

    @Schema(description = "提交理由", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "提交理由不能为空")
    private String submitReason;
}
