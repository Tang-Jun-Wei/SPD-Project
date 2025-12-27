package net.lab1024.sa.admin.module.spd.purchase.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("spd_purchase_main")
public class SpdPurchaseMainEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String purchaseId;
    private String purchaseCode;
    private String supplierId;
    private String supplierName;
    private String warehouseId;
    private Integer sourceType;
    private String sourceCode;
    private Integer purchaseStatus;
    private String createUser;
    private LocalDateTime createTime;
    private String auditUser;
    private LocalDateTime auditTime;
    private String remark;
    private String tenantId;
    @TableLogic
    private Integer delFlag;
    private String updateBy;
    private LocalDateTime updateTime;
}
