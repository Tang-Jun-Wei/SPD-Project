package net.lab1024.sa.admin.module.spd.label.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("spd_label")
public class SpdLabelEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String labelId;
    private String labelCode;
    private String materialId;
    private String batchId;
    private String warehouseId;
    private Integer labelStatus;
    private String tenantId;
    @TableLogic
    private Integer delFlag;
    private String createBy;
    private LocalDateTime createTime;
    private String updateBy;
    private LocalDateTime updateTime;
}
