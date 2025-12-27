package net.lab1024.sa.admin.module.spd.bulkpackage.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;

import java.time.LocalDateTime;

/**
 * 散货打包查询表单
 */
@Data
@Schema(description = "散货打包查询表单")
public class SpdBulkPackageQueryForm extends PageParam {

    @Schema(description = "打包单号")
    private String packageNo;

    @Schema(description = "耗材名称")
    private String materialName;

    @Schema(description = "批号")
    private String batchNo;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;
}
