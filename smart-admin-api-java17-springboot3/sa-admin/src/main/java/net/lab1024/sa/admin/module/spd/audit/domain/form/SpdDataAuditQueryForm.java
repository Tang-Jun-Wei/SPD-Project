package net.lab1024.sa.admin.module.spd.audit.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;

@Data
@Schema(description = "SPD基础资料审核查询表单")
public class SpdDataAuditQueryForm extends PageParam {

    @Schema(description = "审核类型：1=供应商 2=耗材 3=生产厂家 4=调价 5=收费状态")
    private Integer auditType;

    @Schema(description = "目标名称")
    private String targetName;

    @Schema(description = "审核状态：0=待审核 1=已通过 2=已拒绝 3=已撤销")
    private Integer auditStatus;

    @Schema(description = "提交人ID")
    private String submitUser;

    @Schema(description = "提交开始时间")
    private String submitTimeStart;

    @Schema(description = "提交结束时间")
    private String submitTimeEnd;
}
