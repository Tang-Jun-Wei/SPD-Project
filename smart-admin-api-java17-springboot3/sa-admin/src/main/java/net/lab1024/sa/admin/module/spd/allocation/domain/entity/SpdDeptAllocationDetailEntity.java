package net.lab1024.sa.admin.module.spd.allocation.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("spd_dept_allocation_detail")
public class SpdDeptAllocationDetailEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    
    private String allocationId;
    private String materialId;
    private String batchId;
    private Integer allocationQuantity;
    private Integer actualQuantity;
    private String remark;
    private String tenantId;
    
    @TableLogic
    private Integer delFlag;
    
    private LocalDateTime createTime;
}
