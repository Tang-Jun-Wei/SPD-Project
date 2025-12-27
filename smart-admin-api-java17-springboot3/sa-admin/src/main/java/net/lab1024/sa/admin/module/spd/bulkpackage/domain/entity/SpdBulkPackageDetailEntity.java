package net.lab1024.sa.admin.module.spd.bulkpackage.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 散货打包明细表实体
 */
@Data
@TableName("spd_bulk_package_detail")
public class SpdBulkPackageDetailEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long detailId;

    private Long packageId;
    private String labelCode;
    private String serialNo;
    private BigDecimal quantity;
    private Integer status;

    @TableLogic
    private Integer delFlag;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
