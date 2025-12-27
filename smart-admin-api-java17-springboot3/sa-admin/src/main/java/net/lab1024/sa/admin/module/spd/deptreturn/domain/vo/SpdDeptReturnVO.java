package net.lab1024.sa.admin.module.spd.deptreturn.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "科室退库VO")
public class SpdDeptReturnVO {
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "退库单业务ID")
    private String returnId;

    @Schema(description = "退库单号")
    private String returnCode;

    @Schema(description = "退库科室节点ID")
    private String fromNodeId;

    @Schema(description = "退库科室名称")
    private String fromNodeName;

    @Schema(description = "接收仓库节点ID")
    private String toNodeId;

    @Schema(description = "接收仓库名称")
    private String toNodeName;

    @Schema(description = "退库状态：1=待审核 2=已通过 3=已驳回 4=已完成")
    private Integer returnStatus;

    @Schema(description = "退库类型：1=正常退库 2=过期退库 3=损坏退库 4=其他")
    private Integer returnType;

    @Schema(description = "申请人ID")
    private String applyUserId;

    @Schema(description = "申请人姓名")
    private String applyUserName;

    @Schema(description = "申请时间")
    private LocalDateTime applyTime;

    @Schema(description = "审核人ID")
    private String auditUserId;

    @Schema(description = "审核人姓名")
    private String auditUserName;

    @Schema(description = "审核时间")
    private LocalDateTime auditTime;

    @Schema(description = "审核意见")
    private String auditOpinion;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "明细列表")
    private List<DetailVO> detailList;

    @Data
    @Schema(description = "退库明细VO")
    public static class DetailVO {
        @Schema(description = "明细ID")
        private Long id;

        @Schema(description = "耗材ID")
        private String materialId;

        @Schema(description = "耗材名称")
        private String materialName;

        @Schema(description = "批号ID")
        private String batchId;

        @Schema(description = "批号")
        private String batchNo;

        @Schema(description = "退库数量")
        private Integer returnQuantity;

        @Schema(description = "实际退库数量")
        private Integer actualQuantity;

        @Schema(description = "退库原因")
        private String returnReason;

        @Schema(description = "备注")
        private String remark;
    }
}
