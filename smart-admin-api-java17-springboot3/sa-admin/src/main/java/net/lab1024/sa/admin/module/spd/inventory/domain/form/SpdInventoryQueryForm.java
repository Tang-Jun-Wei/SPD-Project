package net.lab1024.sa.admin.module.spd.inventory.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.lab1024.sa.base.common.domain.PageParam;

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "库存查询表单")
public class SpdInventoryQueryForm extends PageParam {
    private String warehouseId;
    private String materialId;
    private String batchId;
}
