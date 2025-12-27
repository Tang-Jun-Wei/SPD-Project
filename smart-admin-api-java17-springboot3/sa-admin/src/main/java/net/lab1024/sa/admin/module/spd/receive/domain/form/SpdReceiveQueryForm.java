package net.lab1024.sa.admin.module.spd.receive.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;
import java.time.LocalDateTime;

@Data
@Schema(description = "收货查询表单")
public class SpdReceiveQueryForm extends PageParam {
    @Schema(description = "收货单号")
    private String receiveCode;
    
    @Schema(description = "验收单号")
    private String acceptanceCode;
    
    @Schema(description = "仓库ID")
    private String warehouseId;
    
    @Schema(description = "收货状态")
    private Integer receiveStatus;
    
    @Schema(description = "收货开始时间")
    private LocalDateTime createTimeStart;
    
    @Schema(description = "收货结束时间")
    private LocalDateTime createTimeEnd;
}
