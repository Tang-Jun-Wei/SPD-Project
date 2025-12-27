package net.lab1024.sa.admin.module.spd.receive.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("spd_receive_main")
public class SpdReceiveMainEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String receiveId;
    private String receiveCode;
    private String acceptanceCode;
    private String warehouseId;
    private Integer receiveStatus;
    private String createUser;
    private LocalDateTime createTime;
    private String remark;
    private String tenantId;
    @TableLogic
    private Integer delFlag;
    private String updateBy;
    private LocalDateTime updateTime;
}
