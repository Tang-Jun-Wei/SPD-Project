package net.lab1024.sa.admin.module.spd.spdpackage.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "打包单VO")
public class SpdPackageVO {
    @Schema(description = "主键ID")
    private Long id;
    
    @Schema(description = "打包单业务ID")
    private String packageId;
    
    @Schema(description = "打包单号")
    private String packageCode;
    
    @Schema(description = "仓库ID")
    private String warehouseId;
    
    @Schema(description = "仓库名称")
    private String warehouseName;
    
    @Schema(description = "打包状态：0=待打包 1=已打包 2=已作废 3=待上架 4=已上架")
    private Integer packageStatus;
    
    @Schema(description = "创建人ID")
    private String createUser;
    
    @Schema(description = "创建人姓名")
    private String createUserName;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "备注")
    private String remark;
}
