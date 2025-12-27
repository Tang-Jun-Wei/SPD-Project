package net.lab1024.sa.admin.module.spd.receive.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "收货明细视图")
public class SpdReceiveDetailVO {
    @Schema(description = "主键ID")
    private Long id;
    
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
    
    @Schema(description = "收货数量")
    private Integer receiveNum;
    
    @Schema(description = "包装规格")
    private String packageSpec;
    
    @Schema(description = "标签码")
    private String labelCode;
}
