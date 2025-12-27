package net.lab1024.sa.admin.module.spd.apply.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "申领视图对象")
public class SpdApplyVO {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "申领业务ID")
    private String applyId;

    @Schema(description = "申领单号")
    private String applyNo;

    @Schema(description = "申领科室ID")
    private String applyDeptId;

    @Schema(description = "申领科室名称")
    private String applyDeptName;

    @Schema(description = "申领人ID")
    private String applyUserId;

    @Schema(description = "申领人姓名")
    private String applyUserName;

    @Schema(description = "申领时间")
    private LocalDateTime applyTime;

    @Schema(description = "申领状态")
    private Integer applyStatus;

    @Schema(description = "审核人ID")
    private String approveUserId;

    @Schema(description = "审核人姓名")
    private String approveUserName;

    @Schema(description = "审核时间")
    private LocalDateTime approveTime;

    @Schema(description = "审核备注")
    private String approveRemark;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "申领明细")
    private List<SpdApplyDetailVO> detailList;
}
