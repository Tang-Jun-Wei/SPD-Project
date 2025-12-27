package net.lab1024.sa.admin.module.spd.bulkpackage.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 散货打包明细VO
 */
@Data
@Schema(description = "散货打包明细VO")
public class SpdBulkPackageDetailVO {

    @Schema(description = "明细ID")
    private Long detailId;

    @Schema(description = "标签编码")
    private String labelCode;

    @Schema(description = "序列号")
    private String serialNo;

    @Schema(description = "数量")
    private BigDecimal quantity;

    @Schema(description = "状态")
    private Integer status;
}
