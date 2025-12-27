package net.lab1024.sa.admin.module.spd.inventory.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("spd_inventory")
public class SpdInventoryEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String inventoryId;
    private String warehouseId;
    private String materialId;
    private String batchId;
    private Integer stockNum;
    private Integer frozenNum;
    private Integer availableNum;
    private String tenantId;
    @TableLogic
    private Integer delFlag;
    private String createBy;
    private LocalDateTime createTime;
    private String updateBy;
    private LocalDateTime updateTime;
}
