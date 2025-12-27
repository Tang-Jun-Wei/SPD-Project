package net.lab1024.sa.admin.module.spd.labeltrack.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 标签追溯查询表单
 *
 * @author 1024创新实验室
 */
@Data
@Schema(description = "标签追溯查询表单")
public class SpdLabelTrackQueryForm {

    @Schema(description = "标签编码")
    @NotBlank(message = "标签编码不能为空")
    private String labelCode;

    @Schema(description = "序列号")
    private String serialNo;

    @Schema(description = "批号")
    private String batchNo;
}
