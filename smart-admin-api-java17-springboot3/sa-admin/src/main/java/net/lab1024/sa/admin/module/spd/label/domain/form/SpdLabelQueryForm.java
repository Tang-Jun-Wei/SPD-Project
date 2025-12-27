package net.lab1024.sa.admin.module.spd.label.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;

@Data
@Schema(description = "标签查询表单")
public class SpdLabelQueryForm extends PageParam {
    @Schema(description = "标签码")
    private String labelCode;
    
    @Schema(description = "耗材ID")
    private String materialId;
    
    @Schema(description = "耗材名称")
    private String materialName;
    
    @Schema(description = "批号")
    private String batchNo;
    
    @Schema(description = "仓库ID")
    private String warehouseId;
    
    @Schema(description = "标签状态")
    private Integer labelStatus;
}
