package net.lab1024.sa.admin.module.spd.audit.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "SPD基础资料审核VO")
public class SpdDataAuditVO {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "审核业务ID")
    private String auditId;

    @Schema(description = "审核类型：1=供应商 2=耗材 3=生产厂家 4=调价 5=收费状态")
    private Integer auditType;

    @Schema(description = "审核类型名称")
    private String auditTypeName;

    @Schema(description = "关联目标业务ID")
    private String targetId;

    @Schema(description = "目标名称")
    private String targetName;

    @Schema(description = "操作类型：1=新增 2=修改")
    private Integer actionType;

    @Schema(description = "操作类型名称")
    private String actionTypeName;

    @Schema(description = "修改前数据（JSON）")
    private String oldData;

    @Schema(description = "新数据或修改后数据（JSON）")
    private String newData;

    @Schema(description = "审核状态：0=待审核 1=已通过 2=已拒绝 3=已撤销")
    private Integer auditStatus;

    @Schema(description = "审核状态名称")
    private String auditStatusName;

    @Schema(description = "提交理由")
    private String submitReason;

    @Schema(description = "审核意见")
    private String auditOpinion;

    @Schema(description = "提交人ID")
    private String submitUser;

    @Schema(description = "提交人姓名")
    private String submitUserName;

    @Schema(description = "提交时间")
    private LocalDateTime submitTime;

    @Schema(description = "审核人ID")
    private String auditUser;

    @Schema(description = "审核人姓名")
    private String auditUserName;

    @Schema(description = "审核时间")
    private LocalDateTime auditTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
