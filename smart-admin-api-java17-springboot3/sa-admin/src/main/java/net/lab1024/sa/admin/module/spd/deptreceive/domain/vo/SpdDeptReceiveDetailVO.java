package net.lab1024.sa.admin.module.spd.deptreceive.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 科室收货明细视图对象
 */
@Data
@Schema(description = "科室收货明细视图")
public class SpdDeptReceiveDetailVO {
    
    @Schema(description = "主键ID")
    private Long id;
    
    @Schema(description = "收货单ID")
    private String receiveId;
    
    @Schema(description = "标签码")
    private String labelCode;
    
    @Schema(description = "耗材ID")
    private String materialId;
    
    @Schema(description = "耗材名称")
    private String materialName;
    
    @Schema(description = "批号ID")
    private String batchId;
    
    @Schema(description = "批号")
    private String batchNo;
    
    @Schema(description = "定数包数量")
    private Integer packageQuantity;
    
    @Schema(description = "收货状态：1=待收货 2=已收货 3=已拒收")
    private Integer receiveStatus;
    
    @Schema(description = "拒收原因")
    private String rejectReason;
    
    @Schema(description = "备注")
    private String remark;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "收货时间")
    private LocalDateTime receiveTime;
}
