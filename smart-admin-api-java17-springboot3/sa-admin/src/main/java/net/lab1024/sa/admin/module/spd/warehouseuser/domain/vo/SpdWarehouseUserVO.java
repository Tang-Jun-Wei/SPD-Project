package net.lab1024.sa.admin.module.spd.warehouseuser.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "SPD仓库人员对应关系VO")
public class SpdWarehouseUserVO {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "关系业务ID")
    private String relationId;

    @Schema(description = "仓库ID")
    private String warehouseId;

    @Schema(description = "仓库名称")
    private String warehouseName;

    @Schema(description = "用户ID")
    private String userId;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "真实姓名")
    private String actualName;

    @Schema(description = "角色类型：1=仓库管理员 2=普通库管员 3=拣货员")
    private Integer roleType;

    @Schema(description = "状态：0=禁用 1=启用")
    private Integer status;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
