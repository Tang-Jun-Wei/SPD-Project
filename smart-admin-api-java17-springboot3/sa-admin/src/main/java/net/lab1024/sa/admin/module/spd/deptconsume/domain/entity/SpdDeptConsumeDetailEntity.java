package net.lab1024.sa.admin.module.spd.deptconsume.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 科室消耗明细表实体
 *
 * @author 1024创新实验室
 */
@Data
@TableName("spd_dept_consume_detail")
public class SpdDeptConsumeDetailEntity {

    /**
     * 明细ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long detailId;

    /**
     * 消耗单ID
     */
    private Long consumeId;

    /**
     * 耗材ID
     */
    private Long materialId;

    /**
     * 耗材编码
     */
    private String materialCode;

    /**
     * 耗材名称
     */
    private String materialName;

    /**
     * 规格型号
     */
    private String specification;

    /**
     * 单位
     */
    private String unit;

    /**
     * 批号
     */
    private String batchNo;

    /**
     * 序列号/标签码
     */
    private String serialNo;

    /**
     * 效期
     */
    private LocalDate expiryDate;

    /**
     * 生产厂家
     */
    private String manufacturer;

    /**
     * 消耗数量
     */
    private BigDecimal quantity;

    /**
     * 单价
     */
    private BigDecimal unitPrice;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 仓库ID
     */
    private Long warehouseId;

    /**
     * 货位ID
     */
    private Long locationId;

    /**
     * 库存ID
     */
    private Long stockId;

    /**
     * 是否已反消耗：0-否，1-是
     */
    private Integer isReversed;

    /**
     * 反消耗时间
     */
    private LocalDateTime reversedTime;

    /**
     * 反消耗人ID
     */
    private Long reversedUserId;

    /**
     * 反消耗人姓名
     */
    private String reversedUserName;

    /**
     * 删除标识：0-正常，1-删除
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
