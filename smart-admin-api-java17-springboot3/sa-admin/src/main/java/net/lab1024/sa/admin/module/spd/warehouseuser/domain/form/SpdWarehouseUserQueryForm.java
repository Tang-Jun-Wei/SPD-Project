package net.lab1024.sa.admin.module.spd.warehouseuser.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;

@Data
@Schema(description = "SPD仓库人员关系查询表单")
public class SpdWarehouseUserQueryForm extends PageParam {

    @Schema(description = "仓库ID")
    private String warehouseId;

    @Schema(description = "用户名或姓名")
    private String userName;

    @Schema(description = "角色类型：1=仓库管理员 2=普通库管员 3=拣货员")
    private Integer roleType;

    @Schema(description = "状态：0=禁用 1=启用")
    private Integer status;
}
