package net.lab1024.sa.admin.module.spd.unit.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "单位VO")
public class SpdUnitVO {
    @Schema(description = "主键ID")
    private Long id;
    
    @Schema(description = "单位编码")
    private String unitCode;
    
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
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
