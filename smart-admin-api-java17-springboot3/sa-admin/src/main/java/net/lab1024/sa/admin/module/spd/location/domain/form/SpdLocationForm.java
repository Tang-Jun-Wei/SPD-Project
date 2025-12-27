package net.lab1024.sa.admin.module.spd.location.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
@Schema(description = "货位表单")
public class SpdLocationForm {
    @Schema(description = "货位ID(编辑时必填)")
    private String locationId;

    @Schema(description = "货位编码", required = true)
    @NotBlank(message = "货位编码不能为空")
    private String locationCode;

    @Schema(description = "货位名称", required = true)
    @NotBlank(message = "货位名称不能为空")
    private String locationName;

    @Schema(description = "关联节点ID", required = true)
    @NotBlank(message = "关联节点不能为空")
    private String nodeId;

    @Schema(description = "货位层级：1=区 2=架 3=层 4=位")
    private Integer locationLevel;

    @Schema(description = "货位类型：1=常温 2=阴凉 3=冷藏 4=冷冻")
    private Integer locationType;

    @Schema(description = "最大容量")
    private Integer maxCapacity;

    @Schema(description = "状态")
    private Integer locationStatus;

    @Schema(description = "备注")
    private String remark;
}
