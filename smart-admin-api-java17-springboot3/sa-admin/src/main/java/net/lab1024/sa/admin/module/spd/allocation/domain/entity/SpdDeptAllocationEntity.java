package net.lab1024.sa.admin.module.spd.allocation.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("spd_dept_allocation")
public class SpdDeptAllocationEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    
    private String allocationId;
    private String allocationCode;
    private String fromNodeId;
    private String toNodeId;
    private Integer allocationStatus;
    private String applyUserId;
    private LocalDateTime applyTime;
    private String auditUserId;
    private LocalDateTime auditTime;
    private String auditOpinion;
    private String remark;
    private String tenantId;
    
    @TableLogic
    private Integer delFlag;
    
    private String createBy;
    private LocalDateTime createTime;
    private String updateBy;
    private LocalDateTime updateTime;
}
