package net.lab1024.sa.admin.module.spd.warehouseuser.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("spd_warehouse_user")
@Schema(description = "SPD仓库人员对应关系实体")
public class SpdWarehouseUserEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "关系业务ID")
    private String relationId;

    @Schema(description = "仓库ID")
    private String warehouseId;

    @Schema(description = "用户ID")
    private String userId;

    @Schema(description = "角色类型：1=仓库管理员 2=普通库管员 3=拣货员")
    private Integer roleType;

    @Schema(description = "状态：0=禁用 1=启用")
    private Integer status;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "租户ID")
    private String tenantId;

    @TableLogic
    @Schema(description = "逻辑删除：0=正常 1=删除")
    private Integer delFlag;

    @Schema(description = "创建人ID")
    private String createBy;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新人ID")
    private String updateBy;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
