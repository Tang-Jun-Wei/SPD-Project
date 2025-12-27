package net.lab1024.sa.admin.module.spd.batch.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;

import java.time.LocalDateTime;

/**
 * 批号信息查询表单
 */
@Data
@Schema(description = "批号信息查询表单")
public class SpdBatchQueryForm extends PageParam {

    @Schema(description = "批号")
    private String batchNo;

    @Schema(description = "耗材ID")
    private String materialId;

    @Schema(description = "耗材名称")
    private String materialName;

    @Schema(description = "供应商名称")
    private String supplierName;

    @Schema(description = "有效期开始")
    private LocalDateTime expiryDateStart;

    @Schema(description = "有效期结束")
    private LocalDateTime expiryDateEnd;

    @Schema(description = "批号状态（0-停用，1-启用）")
    private Integer batchStatus;
}
