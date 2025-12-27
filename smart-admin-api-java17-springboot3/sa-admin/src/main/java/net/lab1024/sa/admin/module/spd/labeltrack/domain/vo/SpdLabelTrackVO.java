package net.lab1024.sa.admin.module.spd.labeltrack.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 标签追溯VO
 *
 * @author 1024创新实验室
 */
@Data
@Schema(description = "标签追溯VO")
public class SpdLabelTrackVO {

    @Schema(description = "标签ID")
    private Long labelId;

    @Schema(description = "标签编码")
    private String labelCode;

    @Schema(description = "耗材编码")
    private String materialCode;

    @Schema(description = "耗材名称")
    private String materialName;

    @Schema(description = "规格型号")
    private String specification;

    @Schema(description = "批号")
    private String batchNo;

    @Schema(description = "序列号")
    private String serialNo;

    @Schema(description = "当前状态")
    private String currentStatus;

    @Schema(description = "当前位置")
    private String currentLocation;

    @Schema(description = "生产厂家")
    private String manufacturer;

    @Schema(description = "供应商")
    private String supplierName;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "轨迹列表")
    private List<SpdLabelTrackDetailVO> trackList;
}
