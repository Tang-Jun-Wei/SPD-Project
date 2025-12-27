package net.lab1024.sa.admin.module.spd.expiry.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;

/**
 * 库存效期预警查询表单
 *
 * @author 1024创新实验室
 */
@Data
@Schema(description = "库存效期预警查询表单")
public class SpdExpiryWarningQueryForm extends PageParam {

    @Schema(description = "耗材名称")
    private String materialName;

    @Schema(description = "批号")
    private String batchNo;

    @Schema(description = "仓库ID")
    private Long warehouseId;

    @Schema(description = "预警级别：1-正常，2-临期，3-即将过期，4-已过期")
    private Integer warningLevel;
}
