package net.lab1024.sa.admin.module.spd.bulkpackage.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 散货打包VO
 */
@Data
@Schema(description = "散货打包VO")
public class SpdBulkPackageVO {

    @Schema(description = "打包单ID")
    private Long packageId;

    @Schema(description = "打包单号")
    private String packageNo;

    @Schema(description = "耗材名称")
    private String materialName;

    @Schema(description = "规格型号")
    private String specification;

    @Schema(description = "批号")
    private String batchNo;

    @Schema(description = "效期")
    private LocalDate expiryDate;

    @Schema(description = "定数包数量")
    private Integer packageQuantity;

    @Schema(description = "单包数量")
    private BigDecimal unitQuantity;

    @Schema(description = "总消耗散货数量")
    private BigDecimal totalQuantity;

    @Schema(description = "仓库名称")
    private String warehouseName;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "创建人")
    private String createUserName;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "明细列表")
    private List<SpdBulkPackageDetailVO> detailList;
}
