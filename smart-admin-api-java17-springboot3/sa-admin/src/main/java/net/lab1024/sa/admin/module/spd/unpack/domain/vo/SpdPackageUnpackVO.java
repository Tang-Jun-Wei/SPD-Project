package net.lab1024.sa.admin.module.spd.unpack.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "定数包拆包VO")
public class SpdPackageUnpackVO {
    @Schema(description = "拆包单ID")
    private Long unpackId;
    @Schema(description = "拆包单号")
    private String unpackNo;
    @Schema(description = "标签编码")
    private String labelCode;
    @Schema(description = "耗材名称")
    private String materialName;
    @Schema(description = "规格型号")
    private String specification;
    @Schema(description = "批号")
    private String batchNo;
    @Schema(description = "定数包数量")
    private BigDecimal packageQuantity;
    @Schema(description = "转换散货数量")
    private BigDecimal bulkQuantity;
    @Schema(description = "仓库名称")
    private String warehouseName;
    @Schema(description = "状态")
    private Integer status;
    @Schema(description = "创建人")
    private String createUserName;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
