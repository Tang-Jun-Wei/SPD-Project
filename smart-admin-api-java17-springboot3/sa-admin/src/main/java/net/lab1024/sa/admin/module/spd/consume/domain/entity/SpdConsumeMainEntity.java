package net.lab1024.sa.admin.module.spd.consume.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("spd_consume_main")
public class SpdConsumeMainEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String consumeId;
    private String consumeCode;
    private String deptId;
    private String materialId;
    private String batchId;
    private Integer consumeNum;
    private Integer consumeStatus;
    private String createUser;
    private LocalDateTime createTime;
    private String remark;
    private String tenantId;
    @TableLogic
    private Integer delFlag;
    private String updateBy;
    private LocalDateTime updateTime;
}
