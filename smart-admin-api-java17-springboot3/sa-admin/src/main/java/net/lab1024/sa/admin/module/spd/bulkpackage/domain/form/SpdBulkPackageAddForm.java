package net.lab1024.sa.admin.module.spd.bulkpackage.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 散货打包新增表单
 */
@Data
@Schema(description = "散货打包新增表单")
public class SpdBulkPackageAddForm {

    @Schema(description = "耗材ID")
    @NotNull(message = "耗材ID不能为空")
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
    @NotNull(message = "批号不能为空")
    private String batchNo;

    @Schema(description = "效期")
    private LocalDate expiryDate;

    @Schema(description = "生产厂家")
    private String manufacturer;

    @Schema(description = "定数包数量（打包几个）")
    @NotNull(message = "定数包数量不能为空")
    private Integer packageQuantity;

    @Schema(description = "单包数量（每个定数包含多少散货）")
    @NotNull(message = "单包数量不能为空")
    private BigDecimal unitQuantity;

    @Schema(description = "仓库ID")
    private Long warehouseId;

    @Schema(description = "仓库名称")
    private String warehouseName;

    @Schema(description = "货位ID")
    private Long locationId;

    @Schema(description = "货位名称")
    private String locationName;

    @Schema(description = "备注")
    private String remark;
}
