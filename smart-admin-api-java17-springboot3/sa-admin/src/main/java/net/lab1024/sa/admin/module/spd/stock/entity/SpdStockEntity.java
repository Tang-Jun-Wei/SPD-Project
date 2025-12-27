package net.lab1024.sa.admin.module.spd.stock.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_spd_stock")
public class SpdStockEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String stockId;
    private String warehouseId;
    private String warehouseName;
    private String materialId;
    private String materialName;
    private String batchId;
    private String batchNo;
    private BigDecimal quantity;
    private BigDecimal lockedQuantity;
    private BigDecimal availableQuantity;
    private LocalDateTime expiryDate;
    private String tenantId;
    private Integer delFlag;
    private String createBy;
    private LocalDateTime createTime;
    private String updateBy;
    private LocalDateTime updateTime;
}
