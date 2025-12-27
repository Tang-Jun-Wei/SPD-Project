package net.lab1024.sa.admin.module.spd.batch.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("spd_material_batch")
public class SpdMaterialBatchEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String batchId;
    private String materialId;
    private String batchNo;
    private LocalDate validDate;
    private String supplierId;
    private String supplierName;
    private String tenantId;
    @TableLogic
    private Integer delFlag;
    private String createBy;
    private LocalDateTime createTime;
    private String updateBy;
    private LocalDateTime updateTime;
}
