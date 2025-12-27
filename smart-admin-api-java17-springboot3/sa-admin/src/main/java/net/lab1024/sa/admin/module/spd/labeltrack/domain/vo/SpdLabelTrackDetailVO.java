package net.lab1024.sa.admin.module.spd.labeltrack.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 标签追溯明细VO
 *
 * @author 1024创新实验室
 */
@Data
@Schema(description = "标签追溯明细VO")
public class SpdLabelTrackDetailVO {

    @Schema(description = "操作类型：1-入库，2-打包，3-出库，4-分发，5-消耗，6-退库，7-调拨")
    private Integer operationType;

    @Schema(description = "操作描述")
    private String operationDesc;

    @Schema(description = "业务单号")
    private String businessNo;

    @Schema(description = "操作人")
    private String operatorName;

    @Schema(description = "操作时间")
    private LocalDateTime operationTime;

    @Schema(description = "来源位置")
    private String fromLocation;

    @Schema(description = "目标位置")
    private String toLocation;

    @Schema(description = "备注")
    private String remark;
}
