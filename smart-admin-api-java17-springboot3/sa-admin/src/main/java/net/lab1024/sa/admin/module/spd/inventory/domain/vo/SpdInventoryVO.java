package net.lab1024.sa.admin.module.spd.inventory.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "库存VO")
public class SpdInventoryVO {
    private Long id;
    private String inventoryId;
    private String warehouseId;
    private String warehouseName;
    private String materialId;
    private String materialName;
    private String batchId;
    private String batchNo;
    private Integer stockNum;
    private Integer frozenNum;
    private Integer availableNum;
    private LocalDateTime updateTime;
}
