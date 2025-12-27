package net.lab1024.sa.admin.module.spd.supplierreturn.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "退供应商VO")
public class SpdSupplierReturnVO {
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "退供应商单业务ID")
    private String supplierReturnId;

    @Schema(description = "退供应商单号")
    private String supplierReturnCode;

    @Schema(description = "退货仓库节点ID")
    private String warehouseNodeId;

    @Schema(description = "退货仓库名称")
    private String warehouseNodeName;

    @Schema(description = "供应商ID")
    private String supplierId;

    @Schema(description = "供应商名称")
    private String supplierName;

    @Schema(description = "退货状态：1=待审核 2=已通过 3=已驳回 4=已完成")
    private Integer returnStatus;

    @Schema(description = "退货类型：1=质量问题 2=过期退货 3=订单错误 4=其他")
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
    @Schema(description = "退货明细VO")
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

        @Schema(description = "退货数量")
        private Integer returnQuantity;

        @Schema(description = "实际退货数量")
        private Integer actualQuantity;

        @Schema(description = "退货原因")
        private String returnReason;

        @Schema(description = "备注")
        private String remark;
    }
}
