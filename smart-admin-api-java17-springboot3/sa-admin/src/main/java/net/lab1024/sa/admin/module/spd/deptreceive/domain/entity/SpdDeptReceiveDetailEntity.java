package net.lab1024.sa.admin.module.spd.deptreceive.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 科室收货明细表实体
 */
@Data
@TableName("spd_dept_receive_detail")
public class SpdDeptReceiveDetailEntity {
    
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    
    private String receiveId;
    
    private String labelCode;
    
    private String materialId;
    
    private String batchId;
    
    private Integer packageQuantity;
    
    private Integer receiveStatus;
    
    private String rejectReason;
    
    private String remark;
    
    private String tenantId;
    
    @TableLogic
    private Integer delFlag;
    
    private LocalDateTime createTime;
    
    private LocalDateTime receiveTime;
}
