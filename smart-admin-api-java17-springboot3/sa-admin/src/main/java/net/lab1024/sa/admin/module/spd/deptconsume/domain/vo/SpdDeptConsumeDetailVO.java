package net.lab1024.sa.admin.module.spd.deptconsume.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 科室消耗明细VO
 *
 * @author 1024创新实验室
 */
@Data
@Schema(description = "科室消耗明细VO")
public class SpdDeptConsumeDetailVO {

    @Schema(description = "明细ID")
    private Long detailId;

    @Schema(description = "消耗单ID")
    private Long consumeId;

    @Schema(description = "耗材ID")
    private Long materialId;

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

    @Schema(description = "序列号/标签码")
    private String serialNo;

    @Schema(description = "效期")
    private LocalDate expiryDate;

    @Schema(description = "生产厂家")
    private String manufacturer;

    @Schema(description = "消耗数量")
    private BigDecimal quantity;

    @Schema(description = "单价")
    private BigDecimal unitPrice;

    @Schema(description = "金额")
    private BigDecimal amount;

    @Schema(description = "仓库ID")
    private Long warehouseId;

    @Schema(description = "货位ID")
    private Long locationId;

    @Schema(description = "库存ID")
    private Long stockId;

    @Schema(description = "是否已反消耗：0-否，1-是")
    private Integer isReversed;

    @Schema(description = "反消耗时间")
    private LocalDateTime reversedTime;

    @Schema(description = "反消耗人姓名")
    private String reversedUserName;
}
