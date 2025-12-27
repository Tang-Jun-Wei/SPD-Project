package net.lab1024.sa.admin.module.spd.deptconsume.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;

import java.time.LocalDateTime;

/**
 * 科室消耗查询表单
 *
 * @author 1024创新实验室
 */
@Data
@Schema(description = "科室消耗查询表单")
public class SpdDeptConsumeQueryForm extends PageParam {

    @Schema(description = "消耗单号")
    private String consumeNo;

    @Schema(description = "科室ID")
    private Long deptId;

    @Schema(description = "消耗类型")
    private Integer consumeType;

    @Schema(description = "患者姓名")
    private String patientName;

    @Schema(description = "患者编号")
    private String patientNo;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;
}
