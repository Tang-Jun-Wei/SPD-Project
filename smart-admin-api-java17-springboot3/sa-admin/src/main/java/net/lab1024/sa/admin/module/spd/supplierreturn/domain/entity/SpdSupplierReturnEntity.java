package net.lab1024.sa.admin.module.spd.supplierreturn.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("spd_supplier_return")
public class SpdSupplierReturnEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    
    private String supplierReturnId;
    private String supplierReturnCode;
    private String warehouseNodeId;
    private String supplierId;
    private Integer returnStatus;
    private Integer returnType;
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
