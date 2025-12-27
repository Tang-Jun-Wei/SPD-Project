package net.lab1024.sa.admin.module.spd.bulkpackage.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 散货打包主表实体
 */
@Data
@TableName("spd_bulk_package")
public class SpdBulkPackageEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long packageId;

    private String packageNo;
    private Long materialId;
    private String materialCode;
    private String materialName;
    private String specification;
    private String unit;
    private String batchNo;
    private LocalDate expiryDate;
    private String manufacturer;
    private Integer packageQuantity;
    private BigDecimal unitQuantity;
    private BigDecimal totalQuantity;
    private Long warehouseId;
    private String warehouseName;
    private Long locationId;
    private String locationName;
    private Integer status;
    private String remark;

    @TableLogic
    private Integer delFlag;

    private Long createUserId;
    private String createUserName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
