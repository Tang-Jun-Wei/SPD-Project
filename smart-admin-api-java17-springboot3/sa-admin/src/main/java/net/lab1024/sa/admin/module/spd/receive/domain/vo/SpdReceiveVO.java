package net.lab1024.sa.admin.module.spd.receive.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "收货单视图")
public class SpdReceiveVO {
    @Schema(description = "主键ID")
    private Long id;
    
    @Schema(description = "收货业务ID")
    private String receiveId;
    
    @Schema(description = "收货单号")
    private String receiveCode;
    
    @Schema(description = "验收单号")
    private String acceptanceCode;
    
    @Schema(description = "仓库ID")
    private String warehouseId;
    
    @Schema(description = "仓库名称")
    private String warehouseName;
    
    @Schema(description = "收货类型：1=定数包 2=散货")
    private Integer receiveType;
    
    @Schema(description = "收货状态：0=待收货 1=已收货 2=已上架")
    private Integer receiveStatus;
    
    @Schema(description = "收货人ID")
    private String createUser;
    
    @Schema(description = "收货人姓名")
    private String createUserName;
    
    @Schema(description = "收货时间")
    private LocalDateTime createTime;
    
    @Schema(description = "上架人ID")
    private String shelfUser;
    
    @Schema(description = "上架时间")
    private LocalDateTime shelfTime;
    
    @Schema(description = "备注")
    private String remark;
    
    @Schema(description = "收货明细")
    private List<SpdReceiveDetailVO> detailList;
}
