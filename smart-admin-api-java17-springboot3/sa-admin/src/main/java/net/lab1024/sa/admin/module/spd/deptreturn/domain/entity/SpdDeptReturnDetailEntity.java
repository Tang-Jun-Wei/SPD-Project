package net.lab1024.sa.admin.module.spd.deptreturn.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("spd_dept_return_detail")
public class SpdDeptReturnDetailEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    
    private String returnId;
    private String materialId;
    private String batchId;
    private Integer returnQuantity;
    private Integer actualQuantity;
    private String returnReason;
    private String remark;
    private String tenantId;
    
    @TableLogic
    private Integer delFlag;
    
    private LocalDateTime createTime;
}
