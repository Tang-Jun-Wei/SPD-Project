package net.lab1024.sa.admin.module.spd.warehouseuser.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@Schema(description = "SPD仓库人员关系表单")
public class SpdWarehouseUserForm {

    @Schema(description = "主键ID（修改时必填）")
    private Long id;

    @Schema(description = "仓库ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "仓库ID不能为空")
    private String warehouseId;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "用户ID不能为空")
    private String userId;

    @Schema(description = "角色类型：1=仓库管理员 2=普通库管员 3=拣货员", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "角色类型不能为空")
    private Integer roleType;

    @Schema(description = "状态：0=禁用 1=启用")
    private Integer status;

    @Schema(description = "备注")
    private String remark;
}
