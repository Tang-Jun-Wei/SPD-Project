package net.lab1024.sa.admin.module.spd.stocklimit.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("spd_stock_limit")
@Schema(description = "SPD库存安全量实体")
public class SpdStockLimitEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "安全量业务ID")
    private String limitId;

    @Schema(description = "耗材业务ID")
    private String materialId;

    @Schema(description = "仓库ID")
    private String warehouseId;

    @Schema(description = "最小库存量（安全下限）")
    private Integer minStock;

    @Schema(description = "最大库存量（安全上限）")
    private Integer maxStock;

    @Schema(description = "预警库存量")
    private Integer warningStock;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "租户ID")
    private String tenantId;

    @TableLogic
    @Schema(description = "逻辑删除：0=正常 1=删除")
    private Integer delFlag;

    @Schema(description = "创建人ID")
    private String createBy;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新人ID")
    private String updateBy;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
