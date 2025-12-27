package net.lab1024.sa.admin.module.spd.deptconsume.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 科室消耗VO
 *
 * @author 1024创新实验室
 */
@Data
@Schema(description = "科室消耗VO")
public class SpdDeptConsumeVO {

    @Schema(description = "消耗单ID")
    private Long consumeId;

    @Schema(description = "消耗单号")
    private String consumeNo;

    @Schema(description = "科室ID")
    private Long deptId;

    @Schema(description = "科室名称")
    private String deptName;

    @Schema(description = "消耗类型：1-正常消耗，2-手术消耗，3-急诊消耗")
    private Integer consumeType;

    @Schema(description = "消耗日期")
    private LocalDateTime consumeDate;

    @Schema(description = "消耗人ID")
    private Long consumeUserId;

    @Schema(description = "消耗人姓名")
    private String consumeUserName;

    @Schema(description = "患者姓名")
    private String patientName;

    @Schema(description = "患者编号")
    private String patientNo;

    @Schema(description = "消耗总数量")
    private BigDecimal totalQuantity;

    @Schema(description = "消耗总金额")
    private BigDecimal totalAmount;

    @Schema(description = "状态：1-正常，2-已反消耗")
    private Integer status;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建人姓名")
    private String createUserName;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "消耗明细列表")
    private List<SpdDeptConsumeDetailVO> detailList;
}
