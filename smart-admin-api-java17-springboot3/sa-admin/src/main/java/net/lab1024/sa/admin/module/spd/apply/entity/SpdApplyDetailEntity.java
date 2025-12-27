package net.lab1024.sa.admin.module.spd.apply.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 申领明细实体类
 */
@Data
@TableName("t_spd_apply_detail")
public class SpdApplyDetailEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String applyId;
    private String materialId;
    private String materialName;
    private String specification;
    private String unit;
    private BigDecimal applyQuantity;
    private BigDecimal approveQuantity;
    private String batchId;
    private String batchNo;
    private Integer delFlag;
    private String createBy;
    private LocalDateTime createTime;
    private String updateBy;
    private LocalDateTime updateTime;
}
