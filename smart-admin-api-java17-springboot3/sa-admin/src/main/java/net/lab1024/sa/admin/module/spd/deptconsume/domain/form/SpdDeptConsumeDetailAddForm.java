package net.lab1024.sa.admin.module.spd.deptconsume.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 科室消耗明细新增表单
 *
 * @author 1024创新实验室
 */
@Data
@Schema(description = "科室消耗明细新增表单")
public class SpdDeptConsumeDetailAddForm {

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
    private String batchNo;

    @Schema(description = "序列号/标签码")
    private String serialNo;

    @Schema(description = "效期")
    private LocalDate expiryDate;

    @Schema(description = "生产厂家")
    private String manufacturer;

    @Schema(description = "消耗数量")
    @NotNull(message = "消耗数量不能为空")
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
}
