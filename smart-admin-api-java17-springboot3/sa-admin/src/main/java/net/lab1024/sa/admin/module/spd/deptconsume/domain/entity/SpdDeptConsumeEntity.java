package net.lab1024.sa.admin.module.spd.deptconsume.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 科室消耗主表实体
 *
 * @author 1024创新实验室
 */
@Data
@TableName("spd_dept_consume")
public class SpdDeptConsumeEntity {

    /**
     * 消耗单ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long consumeId;

    /**
     * 消耗单号
     */
    private String consumeNo;

    /**
     * 科室ID
     */
    private Long deptId;

    /**
     * 科室名称
     */
    private String deptName;

    /**
     * 消耗类型：1-正常消耗，2-手术消耗，3-急诊消耗
     */
    private Integer consumeType;

    /**
     * 消耗日期
     */
    private LocalDateTime consumeDate;

    /**
     * 消耗人ID
     */
    private Long consumeUserId;

    /**
     * 消耗人姓名
     */
    private String consumeUserName;

    /**
     * 患者姓名
     */
    private String patientName;

    /**
     * 患者编号
     */
    private String patientNo;

    /**
     * 消耗总数量
     */
    private BigDecimal totalQuantity;

    /**
     * 消耗总金额
     */
    private BigDecimal totalAmount;

    /**
     * 状态：1-正常，2-已反消耗
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除标识：0-正常，1-删除
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 创建人姓名
     */
    private String createUserName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
