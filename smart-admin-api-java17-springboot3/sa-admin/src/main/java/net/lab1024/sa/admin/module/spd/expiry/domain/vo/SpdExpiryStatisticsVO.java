package net.lab1024.sa.admin.module.spd.expiry.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 库存效期统计VO
 *
 * @author 1024创新实验室
 */
@Data
@Schema(description = "库存效期统计VO")
public class SpdExpiryStatisticsVO {

    @Schema(description = "即将过期数量（30天内）")
    private Long soonExpireCount;

    @Schema(description = "临期数量（60天内）")
    private Long nearExpireCount;

    @Schema(description = "已过期数量")
    private Long expiredCount;

    @Schema(description = "正常数量")
    private Long normalCount;

    @Schema(description = "总数量")
    private Long totalCount;
}
