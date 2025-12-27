package net.lab1024.sa.admin.module.spd.spdreturn.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("spd_return_main")
public class SpdReturnMainEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String returnId;
    private String returnCode;
    private String warehouseId;
    private String materialId;
    private String batchId;
    private Integer returnNum;
    private Integer returnStatus;
    private String createUser;
    private LocalDateTime createTime;
    private String remark;
    private String tenantId;
    @TableLogic
    private Integer delFlag;
    private String updateBy;
    private LocalDateTime updateTime;
}
