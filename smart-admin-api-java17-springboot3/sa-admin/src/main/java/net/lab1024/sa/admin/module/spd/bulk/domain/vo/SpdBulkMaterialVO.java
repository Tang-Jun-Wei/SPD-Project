package net.lab1024.sa.admin.module.spd.bulk.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "散货视图")
public class SpdBulkMaterialVO {
    @Schema(description = "主键ID")
    private Long id;
    
    @Schema(description = "散货业务ID")
    private String bulkId;
    
    @Schema(description = "耗材ID")
    private String materialId;
    
    @Schema(description = "耗材名称")
    private String materialName;
    
    @Schema(description = "规格型号")
    private String specification;
    
    @Schema(description = "批号ID")
    private String batchId;
    
    @Schema(description = "批号")
    private String batchNo;
    
    @Schema(description = "入库总数量")
    private Integer totalNum;
    
    @Schema(description = "已使用数量")
    private Integer usedNum;
    
    @Schema(description = "剩余数量")
    private Integer remainingNum;
    
    @Schema(description = "仓库ID")
    private String warehouseId;
    
    @Schema(description = "仓库名称")
    private String warehouseName;
    
    @Schema(description = "入库时间")
    private LocalDateTime createTime;
}
