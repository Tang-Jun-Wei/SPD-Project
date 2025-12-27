package net.lab1024.sa.admin.module.spd.deptconsume.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 科室消耗新增表单
 *
 * @author 1024创新实验室
 */
@Data
@Schema(description = "科室消耗新增表单")
public class SpdDeptConsumeAddForm {

    @Schema(description = "科室ID")
    @NotNull(message = "科室ID不能为空")
    private Long deptId;

    @Schema(description = "科室名称")
    private String deptName;

    @Schema(description = "消耗类型：1-正常消耗，2-手术消耗，3-急诊消耗")
    @NotNull(message = "消耗类型不能为空")
    private Integer consumeType;

    @Schema(description = "消耗日期")
    @NotNull(message = "消耗日期不能为空")
    private LocalDateTime consumeDate;

    @Schema(description = "患者姓名")
    private String patientName;

    @Schema(description = "患者编号")
    private String patientNo;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "消耗明细列表")
    @NotEmpty(message = "消耗明细不能为空")
    @Valid
    private List<SpdDeptConsumeDetailAddForm> detailList;
}
