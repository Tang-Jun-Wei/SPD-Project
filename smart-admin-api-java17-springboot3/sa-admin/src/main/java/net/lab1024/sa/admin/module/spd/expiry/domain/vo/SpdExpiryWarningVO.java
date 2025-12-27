package net.lab1024.sa.admin.module.spd.expiry.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 库存效期预警VO
 *
 * @author 1024创新实验室
 */
@Data
@Schema(description = "库存效期预警VO")
public class SpdExpiryWarningVO {

    @Schema(description = "库存ID")
    private Long stockId;

    @Schema(description = "耗材编码")
    private String materialCode;

    @Schema(description = "耗材名称")
    private String materialName;

    @Schema(description = "规格型号")
    private String specification;

    @Schema(description = "单位")
    private String unit;

    @Schema(description = "批号")
    private String batchNo;

    @Schema(description = "效期")
    private LocalDate expiryDate;

    @Schema(description = "剩余天数")
    private Integer remainDays;

    @Schema(description = "库存数量")
    private BigDecimal quantity;

    @Schema(description = "仓库名称")
    private String warehouseName;

    @Schema(description = "货位名称")
    private String locationName;

    @Schema(description = "生产厂家")
    private String manufacturer;

    @Schema(description = "预警级别：1-正常，2-临期，3-即将过期，4-已过期")
    private Integer warningLevel;
}
