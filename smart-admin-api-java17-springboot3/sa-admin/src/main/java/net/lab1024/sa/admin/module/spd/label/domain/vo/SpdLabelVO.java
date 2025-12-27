package net.lab1024.sa.admin.module.spd.label.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "标签视图")
public class SpdLabelVO {
    @Schema(description = "主键ID")
    private Long id;
    
    @Schema(description = "标签码")
    private String labelCode;
    
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
    
    @Schema(description = "包装规格")
    private String packageSpec;
    
    @Schema(description = "单包数量")
    private Integer packageNum;
    
    @Schema(description = "仓库ID")
    private String warehouseId;
    
    @Schema(description = "仓库名称")
    private String warehouseName;
    
    @Schema(description = "标签状态：1=可用 2=已领用 3=已作废 4=已过期")
    private Integer labelStatus;
    
    @Schema(description = "生成时间")
    private LocalDateTime createTime;
    
    @Schema(description = "领用时间")
    private LocalDateTime useTime;
}
