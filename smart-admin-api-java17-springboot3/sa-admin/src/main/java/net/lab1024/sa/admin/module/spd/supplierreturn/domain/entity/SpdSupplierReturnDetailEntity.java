package net.lab1024.sa.admin.module.spd.supplierreturn.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("spd_supplier_return_detail")
public class SpdSupplierReturnDetailEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    
    private String supplierReturnId;
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
