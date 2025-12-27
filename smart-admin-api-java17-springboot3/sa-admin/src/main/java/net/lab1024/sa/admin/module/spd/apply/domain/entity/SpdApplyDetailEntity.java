package net.lab1024.sa.admin.module.spd.apply.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("spd_apply_detail")
public class SpdApplyDetailEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String applyCode;
    private String materialId;
    private String batchId;
    private Integer applyNum;
    private String tenantId;
    @TableLogic
    private Integer delFlag;
    private String createBy;
    private LocalDateTime createTime;
}
