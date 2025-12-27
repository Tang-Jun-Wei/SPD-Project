package net.lab1024.sa.admin.module.spd.label.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("spd_label")
public class SpdLabelEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String labelCode;
    private String materialId;
    private String batchId;
    private String packageSpec;
    private Integer packageNum;
    private String warehouseId;
    private Integer labelStatus;
    private LocalDateTime createTime;
    private LocalDateTime useTime;
    private String tenantId;
    @TableLogic
    private Integer delFlag;
    private String updateBy;
    private LocalDateTime updateTime;
}
