package net.lab1024.sa.admin.module.spd.apply.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("spd_apply_main")
public class SpdApplyMainEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String applyId;
    private String applyCode;
    private String deptId;
    private String warehouseId;
    private Integer applyStatus;
    private String createUser;
    private LocalDateTime createTime;
    private String auditUser;
    private LocalDateTime auditTime;
    private String remark;
    private String tenantId;
    @TableLogic
    private Integer delFlag;
    private String updateBy;
    private LocalDateTime updateTime;
}
