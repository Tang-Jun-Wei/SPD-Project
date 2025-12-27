package net.lab1024.sa.admin.module.spd.material.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 耗材信息视图对象
 */
@Data
@Schema(description = "耗材信息视图对象")
public class SpdMaterialVO {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "耗材业务ID")
    private String materialId;

    @Schema(description = "耗材名称")
    private String materialName;

    @Schema(description = "耗材编码")
    private String materialCode;

    @Schema(description = "规格型号")
    private String specification;

    @Schema(description = "单位")
    private String unit;

    @Schema(description = "生产厂家")
    private String manufacturer;

    @Schema(description = "供应商ID")
    private String supplierId;

    @Schema(description = "供应商名称")
    private String supplierName;

    @Schema(description = "单价")
    private BigDecimal unitPrice;

    @Schema(description = "是否集采（0-否，1-是）")
    private Integer isGroupBuy;

    @Schema(description = "UDI编码")
    private String udiCode;

    @Schema(description = "注册证号")
    private String registrationNo;

    @Schema(description = "注册证有效期")
    private LocalDateTime registrationExpiry;

    @Schema(description = "耗材分类")
    private String category;

    @Schema(description = "耗材状态（0-停用，1-启用）")
    private Integer materialStatus;

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
