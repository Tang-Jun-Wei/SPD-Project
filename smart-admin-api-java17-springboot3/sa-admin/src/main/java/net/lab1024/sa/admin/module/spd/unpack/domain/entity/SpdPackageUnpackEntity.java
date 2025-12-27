package net.lab1024.sa.admin.module.spd.unpack.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("spd_package_unpack")
public class SpdPackageUnpackEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private Long unpackId;
    private String unpackNo;
    private String labelCode;
    private Long materialId;
    private String materialName;
    private String specification;
    private String batchNo;
    private BigDecimal packageQuantity;
    private BigDecimal bulkQuantity;
    private Long warehouseId;
    private String warehouseName;
    private Integer status;
    private String remark;
    @TableLogic
    private Integer delFlag;
    private Long createUserId;
    private String createUserName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
