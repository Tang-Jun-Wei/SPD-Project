package net.lab1024.sa.admin.module.spd.deptreceive.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 科室收货主表实体
 */
@Data
@TableName("spd_dept_receive")
public class SpdDeptReceiveEntity {
    
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    
    private String receiveId;
    
    private String receiveCode;
    
    private String deptId;
    
    private String warehouseId;
    
    private Integer receiveType;
    
    private Integer receiveStatus;
    
    private Integer totalQuantity;
    
    private Integer receivedQuantity;
    
    private Integer rejectedQuantity;
    
    private String remark;
    
    private String tenantId;
    
    @TableLogic
    private Integer delFlag;
    
    private String createBy;
    
    private LocalDateTime createTime;
    
    private String receiveBy;
    
    private LocalDateTime receiveTime;
    
    private String updateBy;
    
    private LocalDateTime updateTime;
}
