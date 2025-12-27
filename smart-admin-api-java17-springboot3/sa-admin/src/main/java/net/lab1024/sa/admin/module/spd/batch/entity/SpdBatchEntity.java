package net.lab1024.sa.admin.module.spd.batch.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 批号信息实体类
 */
@Data
@TableName("t_spd_batch")
public class SpdBatchEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 批号业务ID
     */
    private String batchId;

    /**
     * 批号
     */
    private String batchNo;

    /**
     * 耗材ID
     */
    private String materialId;

    /**
     * 耗材名称
     */
    private String materialName;

    /**
     * 生产日期
     */
    private LocalDateTime productionDate;

    /**
     * 有效期至
     */
    private LocalDateTime expiryDate;

    /**
     * 灭菌日期
     */
    private LocalDateTime sterilizationDate;

    /**
     * 灭菌批号
     */
    private String sterilizationBatchNo;

    /**
     * 采购价格
     */
    private BigDecimal purchasePrice;

    /**
     * 供应商ID
     */
    private String supplierId;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 批号状态（0-停用，1-启用）
     */
    private Integer batchStatus;

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
