package net.lab1024.sa.admin.module.spd.acceptance.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 验收明细视图对象
 */
@Data
@Schema(description = "验收明细视图")
public class SpdAcceptanceDetailVO {
    
    @Schema(description = "主键ID")
    private Long id;
    
    @Schema(description = "验收ID")
    private String acceptanceId;
    
    @Schema(description = "耗材ID")
    private String materialId;
    
    @Schema(description = "耗材名称")
    private String materialName;
    
    @Schema(description = "批号ID")
    private String batchId;
    
    @Schema(description = "批号")
    private String batchNo;
    
    @Schema(description = "验收数量")
    private BigDecimal acceptanceQuantity;
    
    @Schema(description = "合格数量")
    private BigDecimal qualifiedQuantity;
    
    @Schema(description = "不合格数量")
    private BigDecimal unqualifiedQuantity;
    
    @Schema(description = "单价")
    private BigDecimal unitPrice;
    
    @Schema(description = "总价")
    private BigDecimal totalPrice;
    
    @Schema(description = "质量状态")
    private String qualityStatus;
    
    @Schema(description = "备注")
    private String remark;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
