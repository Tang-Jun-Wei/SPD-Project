package net.lab1024.sa.admin.module.spd.batch.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 批号信息视图对象
 */
@Data
@Schema(description = "批号信息视图对象")
public class SpdBatchVO {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "批号业务ID")
    private String batchId;

    @Schema(description = "批号")
    private String batchNo;

    @Schema(description = "耗材ID")
    private String materialId;

    @Schema(description = "耗材名称")
    private String materialName;

    @Schema(description = "生产日期")
    private LocalDateTime productionDate;

    @Schema(description = "有效期至")
    private LocalDateTime expiryDate;

    @Schema(description = "灭菌日期")
    private LocalDateTime sterilizationDate;

    @Schema(description = "灭菌批号")
    private String sterilizationBatchNo;

    @Schema(description = "采购价格")
    private BigDecimal purchasePrice;

    @Schema(description = "供应商ID")
    private String supplierId;

    @Schema(description = "供应商名称")
    private String supplierName;

    @Schema(description = "批号状态（0-停用，1-启用）")
    private Integer batchStatus;

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新人")
    private String updateBy;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "备注")
    private String remark;
}
