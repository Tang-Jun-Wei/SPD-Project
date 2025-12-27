package net.lab1024.sa.admin.module.spd.expiry.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;

@Data
@Schema(description = "SPD库存效期监控查询表单")
public class SpdExpiryMonitorQueryForm extends PageParam {

    @Schema(description = "耗材名称")
    private String materialName;

    @Schema(description = "仓库ID")
    private String warehouseId;

    @Schema(description = "库存类型：1=定数包 2=散货")
    private Integer stockType;

    @Schema(description = "预警级别：1=30天内到期 2=60天内到期 3=90天内到期 4=已过期")
    private Integer warningLevel;

    @Schema(description = "批号")
    private String batchNo;
}
