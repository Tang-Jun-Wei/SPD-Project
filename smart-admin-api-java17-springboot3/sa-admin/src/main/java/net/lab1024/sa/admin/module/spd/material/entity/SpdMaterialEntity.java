package net.lab1024.sa.admin.module.spd.material.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 耗材信息实体类
 */
@Data
@TableName("t_spd_material")
public class SpdMaterialEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 耗材业务ID
     */
    private String materialId;

    /**
     * 耗材名称
     */
    private String materialName;

    /**
     * 耗材编码
     */
    private String materialCode;

    /**
     * 规格型号
     */
    private String specification;

    /**
     * 单位
     */
    private String unit;

    /**
     * 生产厂家
     */
    private String manufacturer;

    /**
     * 供应商ID
     */
    private String supplierId;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 单价
     */
    private BigDecimal unitPrice;

    /**
     * 是否集采（0-否，1-是）
     */
    private Integer isGroupBuy;

    /**
     * UDI编码
     */
    private String udiCode;

    /**
     * 注册证号
     */
    private String registrationNo;

    /**
     * 注册证有效期
     */
    private LocalDateTime registrationExpiry;

    /**
     * 耗材分类
     */
    private String category;

    /**
     * 耗材状态（0-停用，1-启用）
     */
    private Integer materialStatus;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 删除标记（0-未删除，1-已删除）
     */
    private Integer delFlag;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    private String remark;
}
