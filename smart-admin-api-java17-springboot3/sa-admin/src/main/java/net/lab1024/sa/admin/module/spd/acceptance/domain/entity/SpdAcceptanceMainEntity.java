package net.lab1024.sa.admin.module.spd.acceptance.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("spd_acceptance_main")
public class SpdAcceptanceMainEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String acceptanceId;
    private String acceptanceCode;
    private String purchaseCode;
    private String warehouseId;
    private Integer acceptanceStatus;
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
