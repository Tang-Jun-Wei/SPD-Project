package net.lab1024.sa.admin.module.spd.location.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("spd_location")
public class SpdLocationEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    
    private String locationId;
    private String locationCode;
    private String locationName;
    private String nodeId;
    private Integer locationLevel;
    private Integer locationType;
    private Integer maxCapacity;
    private Integer currentQuantity;
    private Integer locationStatus;
    private String remark;
    private String tenantId;
    
    @TableLogic
    private Integer delFlag;
    
    private String createBy;
    private LocalDateTime createTime;
    private String updateBy;
    private LocalDateTime updateTime;
}
