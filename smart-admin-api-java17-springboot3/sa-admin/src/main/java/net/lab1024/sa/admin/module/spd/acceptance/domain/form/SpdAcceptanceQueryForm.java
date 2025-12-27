package net.lab1024.sa.admin.module.spd.acceptance.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;
import java.time.LocalDateTime;

/**
 * 验收管理查询表单
 */
@Data
@Schema(description = "验收管理查询表单")
public class SpdAcceptanceQueryForm extends PageParam {
    
    @Schema(description = "验收单号")
    private String acceptanceNo;
    
    @Schema(description = "采购ID")
    private String purchaseId;
    
    @Schema(description = "仓库ID")
    private String warehouseId;
    
    @Schema(description = "验收状态")
    private Integer acceptanceStatus;
    
    @Schema(description = "验收人")
    private String acceptanceBy;
    
    @Schema(description = "开始日期")
    private LocalDateTime startDate;
    
    @Schema(description = "结束日期")
    private LocalDateTime endDate;
}
