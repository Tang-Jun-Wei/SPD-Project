package net.lab1024.sa.admin.module.spd.spdpackage.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("spd_package_main")
public class SpdPackageMainEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String packageId;
    private String packageCode;
    private String warehouseId;
    private Integer packageStatus;
    private String createUser;
    private LocalDateTime createTime;
    private String remark;
    private String tenantId;
    @TableLogic
    private Integer delFlag;
    private String updateBy;
    private LocalDateTime updateTime;
}
