package net.lab1024.sa.admin.module.spd.acceptance.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 验收管理视图对象
 */
@Data
@Schema(description = "验收管理视图")
public class SpdAcceptanceVO {
    
    @Schema(description = "主键ID")
    private Long id;
    
    @Schema(description = "验收业务ID")
    private String acceptanceId;
    
    @Schema(description = "验收单号")
    private String acceptanceNo;
    
    @Schema(description = "采购ID")
    private String purchaseId;
    
    @Schema(description = "采购单号")
    private String purchaseNo;
    
    @Schema(description = "仓库ID")
    private String warehouseId;
    
    @Schema(description = "仓库名称")
    private String warehouseName;
    
    @Schema(description = "验收日期")
    private LocalDateTime acceptanceDate;
    
    @Schema(description = "验收人")
    private String acceptanceBy;
    
    @Schema(description = "验收人姓名")
    private String acceptanceByName;
    
    @Schema(description = "验收状态：1-待验收，2-验收中，3-验收完成")
    private Integer acceptanceStatus;
    
    @Schema(description = "总金额")
    private BigDecimal totalAmount;
    
    @Schema(description = "备注")
    private String remark;
    
    @Schema(description = "创建人")
    private String createBy;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "验收明细列表")
    private List<SpdAcceptanceDetailVO> detailList;
}
