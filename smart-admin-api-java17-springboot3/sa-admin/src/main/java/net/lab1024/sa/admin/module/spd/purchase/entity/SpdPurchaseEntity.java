package net.lab1024.sa.admin.module.spd.purchase.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_spd_purchase")
public class SpdPurchaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String purchaseId;
    private String purchaseNo;
    private String supplierId;
    private String supplierName;
    private String warehouseId;
    private String warehouseName;
    private BigDecimal totalAmount;
    private Integer purchaseStatus;
    private String createBy;
    private LocalDateTime createTime;
    private String updateBy;
    private LocalDateTime updateTime;
    private String tenantId;
    private Integer delFlag;
    private String remark;
}
