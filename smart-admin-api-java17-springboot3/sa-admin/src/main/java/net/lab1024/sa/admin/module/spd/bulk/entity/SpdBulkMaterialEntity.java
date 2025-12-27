package net.lab1024.sa.admin.module.spd.bulk.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("spd_bulk_material")
public class SpdBulkMaterialEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String bulkId;
    private String materialId;
    private String batchId;
    private Integer totalNum;
    private Integer usedNum;
    private Integer remainingNum;
    private String warehouseId;
    private LocalDateTime createTime;
    private String tenantId;
    @TableLogic
    private Integer delFlag;
    private String updateBy;
    private LocalDateTime updateTime;
}
