package net.lab1024.sa.admin.module.spd.acceptance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 验收管理实体类
 */
@Data
@TableName("spd_acceptance")
public class SpdAcceptanceEntity {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String acceptanceId;
    
    private String acceptanceNo;
    
    private String purchaseId;
    
    private String warehouseId;
    
    private LocalDateTime acceptanceDate;
    
    private String acceptanceBy;
    
    private Integer acceptanceStatus;
    
    private BigDecimal totalAmount;
    
    private String remark;
    
    private String tenantId;
    
    private Integer delFlag;
    
    private String createBy;
    
    private LocalDateTime createTime;
    
    private String updateBy;
    
    private LocalDateTime updateTime;
}
