package net.lab1024.sa.admin.module.spd.expiry.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Schema(description = "SPD库存效期监控VO")
public class SpdExpiryMonitorVO {

    @Schema(description = "标签ID（定数包）")
    private String labelId;

    @Schema(description = "批号ID（散货）")
    private String batchId;

    @Schema(description = "库存类型：1=定数包 2=散货")
    private Integer stockType;

    @Schema(description = "耗材业务ID")
    private String materialId;

    @Schema(description = "耗材名称")
    private String materialName;

    @Schema(description = "规格型号")
    private String specification;

    @Schema(description = "仓库ID")
    private String warehouseId;

    @Schema(description = "仓库名称")
    private String warehouseName;

    @Schema(description = "批号")
    private String batchNo;

    @Schema(description = "生产日期")
    private LocalDate productionDate;

    @Schema(description = "有效期至")
    private LocalDate expiryDate;

    @Schema(description = "剩余天数")
    private Integer remainingDays;

    @Schema(description = "预警级别：1=30天内到期 2=60天内到期 3=90天内到期 4=已过期")
    private Integer warningLevel;

    @Schema(description = "库存数量")
    private Integer stockQuantity;

    @Schema(description = "供应商名称")
    private String supplierName;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
