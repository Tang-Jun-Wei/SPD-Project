package net.lab1024.sa.admin.module.spd.audit.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("spd_data_audit")
@Schema(description = "SPD基础资料审核实体")
public class SpdDataAuditEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "审核业务ID")
    private String auditId;

    @Schema(description = "审核类型：1=供应商 2=耗材 3=生产厂家 4=调价 5=收费状态")
    private Integer auditType;

    @Schema(description = "关联目标业务ID")
    private String targetId;

    @Schema(description = "目标名称")
    private String targetName;

    @Schema(description = "操作类型：1=新增 2=修改")
    private Integer actionType;

    @Schema(description = "修改前数据（JSON）")
    private String oldData;

    @Schema(description = "新数据或修改后数据（JSON）")
    private String newData;

    @Schema(description = "审核状态：0=待审核 1=已通过 2=已拒绝 3=已撤销")
    private Integer auditStatus;

    @Schema(description = "提交理由")
    private String submitReason;

    @Schema(description = "审核意见")
    private String auditOpinion;

    @Schema(description = "提交人ID")
    private String submitUser;

    @Schema(description = "提交时间")
    private LocalDateTime submitTime;

    @Schema(description = "审核人ID")
    private String auditUser;

    @Schema(description = "审核时间")
    private LocalDateTime auditTime;

    @Schema(description = "租户ID")
    private String tenantId;

    @TableLogic
    @Schema(description = "逻辑删除：0=正常 1=删除")
    private Integer delFlag;

    @Schema(description = "创建人ID")
    private String createBy;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新人ID")
    private String updateBy;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
