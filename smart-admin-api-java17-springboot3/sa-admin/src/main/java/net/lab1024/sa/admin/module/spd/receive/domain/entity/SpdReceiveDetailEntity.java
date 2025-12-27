package net.lab1024.sa.admin.module.spd.receive.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("spd_receive_detail")
public class SpdReceiveDetailEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String receiveCode;
    private String materialId;
    private String batchId;
    private Integer receiveNum;
    private String packageSpec;
    private String labelCode;
    private String tenantId;
    @TableLogic
    private Integer delFlag;
    private String createBy;
    private LocalDateTime createTime;
}
