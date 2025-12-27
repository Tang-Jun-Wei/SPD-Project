package net.lab1024.sa.admin.module.spd.apply.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;
import java.time.LocalDateTime;

@Data
@Schema(description = "申领查询表单")
public class SpdApplyQueryForm extends PageParam {

    @Schema(description = "申领单号")
    private String applyNo;

    @Schema(description = "申领科室ID")
    private String applyDeptId;

    @Schema(description = "申领状态")
    private Integer applyStatus;

    @Schema(description = "申领时间开始")
    private LocalDateTime applyTimeStart;

    @Schema(description = "申领时间结束")
    private LocalDateTime applyTimeEnd;
}
