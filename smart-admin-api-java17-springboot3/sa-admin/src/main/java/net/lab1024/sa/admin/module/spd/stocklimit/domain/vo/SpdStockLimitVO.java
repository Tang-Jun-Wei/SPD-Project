package net.lab1024.sa.admin.module.spd.stocklimit.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "SPD库存安全量VO")
public class SpdStockLimitVO {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "安全量业务ID")
    private String limitId;

    @Schema(description = "耗材业务ID")
    private String materialId;

    @Schema(description = "耗材名称")
    private String materialName;

    @Schema(description = "仓库ID")
    private String warehouseId;

    @Schema(description = "仓库名称")
    private String warehouseName;

    @Schema(description = "最小库存量（安全下限）")
    private Integer minStock;

    @Schema(description = "最大库存量（安全上限）")
    private Integer maxStock;

    @Schema(description = "预警库存量")
    private Integer warningStock;

    @Schema(description = "当前库存量")
    private Integer currentStock;

    @Schema(description = "库存状态：0=正常 1=低于预警 2=低于下限 3=超过上限")
    private Integer stockStatus;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
