package net.lab1024.sa.admin.module.spd.deptreceive.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 科室收货单视图对象
 */
@Data
@Schema(description = "科室收货单视图")
public class SpdDeptReceiveVO {
    
    @Schema(description = "主键ID")
    private Long id;
    
    @Schema(description = "收货单ID")
    private String receiveId;
    
    @Schema(description = "收货单号")
    private String receiveCode;
    
    @Schema(description = "科室ID")
    private String deptId;
    
    @Schema(description = "科室名称")
    private String deptName;
    
    @Schema(description = "仓库ID")
    private String warehouseId;
    
    @Schema(description = "仓库名称")
    private String warehouseName;
    
    @Schema(description = "收货类型：1=正常收货 2=紧急收货")
    private Integer receiveType;
    
    @Schema(description = "收货状态：1=待收货 2=已收货 3=部分拒收")
    private Integer receiveStatus;
    
    @Schema(description = "总数量")
    private Integer totalQuantity;
    
    @Schema(description = "已收数量")
    private Integer receivedQuantity;
    
    @Schema(description = "拒收数量")
    private Integer rejectedQuantity;
    
    @Schema(description = "备注")
    private String remark;
    
    @Schema(description = "创建人")
    private String createBy;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "收货人")
    private String receiveBy;
    
    @Schema(description = "收货时间")
    private LocalDateTime receiveTime;
    
    @Schema(description = "收货明细列表")
    private List<SpdDeptReceiveDetailVO> detailList;
}
