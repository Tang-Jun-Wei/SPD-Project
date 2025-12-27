package net.lab1024.sa.admin.module.spd.acceptance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 验收明细实体类
 */
@Data
@TableName("spd_acceptance_detail")
public class SpdAcceptanceDetailEntity {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String acceptanceId;
    
    private String materialId;
    
    private String batchId;
    
    private BigDecimal acceptanceQuantity;
    
    private BigDecimal qualifiedQuantity;
    
    private BigDecimal unqualifiedQuantity;
    
    private BigDecimal unitPrice;
    
    private BigDecimal totalPrice;
    
    private String qualityStatus;
    
    private String remark;
    
    private Integer delFlag;
    
    private String createBy;
    
    private LocalDateTime createTime;
    
    private String updateBy;
    
    private LocalDateTime updateTime;
}
