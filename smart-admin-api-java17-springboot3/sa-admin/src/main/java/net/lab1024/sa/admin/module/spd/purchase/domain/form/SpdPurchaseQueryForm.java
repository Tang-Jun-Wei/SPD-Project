package net.lab1024.sa.admin.module.spd.purchase.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;
import java.time.LocalDateTime;

@Data
@Schema(description = "采购查询表单")
public class SpdPurchaseQueryForm extends PageParam {
    @Schema(description = "采购单号")
    private String purchaseNo;
    
    @Schema(description = "供应商ID")
    private String supplierId;
    
    @Schema(description = "仓库ID")
    private String warehouseId;
    
    @Schema(description = "采购状态")
    private Integer purchaseStatus;
    
    @Schema(description = "创建时间开始")
    private LocalDateTime createTimeStart;
    
    @Schema(description = "创建时间结束")
    private LocalDateTime createTimeEnd;
}
