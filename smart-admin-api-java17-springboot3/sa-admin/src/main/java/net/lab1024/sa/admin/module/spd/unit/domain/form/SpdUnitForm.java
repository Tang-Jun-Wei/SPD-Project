package net.lab1024.sa.admin.module.spd.unit.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "单位表单")
public class SpdUnitForm {
    @Schema(description = "主键ID(修改时必填)")
    private Long id;
    
    @Schema(description = "单位编码")
    private String unitCode;
    
    @NotBlank(message = "单位名称不能为空")
    @Schema(description = "单位名称")
    private String unitName;
    
    @Schema(description = "单位简称")
    private String unitAbbr;
    
    @Schema(description = "单位类型(1=基本单位/2=辅助单位)")
    private Integer unitType;
    
    @Schema(description = "是否默认(1=是/0=否)")
    private Integer isDefault;
    
    @Schema(description = "排序号")
    private Integer sort;
    
    @Schema(description = "状态(1=启用/0=停用)")
    private Integer status;
    
    @Schema(description = "备注")
    private String remark;
}
