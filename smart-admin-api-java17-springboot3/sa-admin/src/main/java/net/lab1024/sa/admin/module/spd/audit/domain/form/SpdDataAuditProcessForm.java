package net.lab1024.sa.admin.module.spd.audit.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
@Schema(description = "SPD基础资料审核处理表单")
public class SpdDataAuditProcessForm {

    @Schema(description = "审核记录ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "审核记录ID不能为空")
    private Long id;

    @Schema(description = "审核状态：1=通过 2=拒绝", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "审核状态不能为空")
    private Integer auditStatus;

    @Schema(description = "审核意见")
    private String auditOpinion;
}
