package net.lab1024.sa.admin.module.spd.batch.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.lab1024.sa.base.common.domain.PageParam;

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "SPD批号查询表单")
public class SpdMaterialBatchQueryForm extends PageParam {
    @Schema(description = "耗材ID")
    private String materialId;
    
    @Schema(description = "批号")
    private String batchNo;
    
    @Schema(description = "供应商ID")
    private String supplierId;
}
