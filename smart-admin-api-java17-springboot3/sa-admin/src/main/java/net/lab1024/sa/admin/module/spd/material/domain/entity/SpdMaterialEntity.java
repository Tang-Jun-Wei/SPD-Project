package net.lab1024.sa.admin.module.spd.material.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * SPD耗材管理 - 实体类
 * 
 * 功能说明：
 * 1. 对应数据库表 spd_material（耗材基础表）
 * 2. 支持集采标识和集采类型（省集采/市集采/合同采购）
 * 3. 支持UDI编码、注册证号、有效期管理
 * 4. 支持高值耗材标识
 * 5. 精细化状态管理（可用/业务停用/质量停用）
 * 
 * 业务规则：
 * - 耗材业务ID格式：MAT+年月日+6位序号
 * - 耗材名称+规格+型号+删除状态 唯一
 * - 支持多租户隔离（tenant_id）
 * - 支持逻辑删除（del_flag）
 * 
 * @since 2025-12-27
 */
@Data
@TableName("spd_material")
public class SpdMaterialEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String materialId;

    private String materialName;

    private String spec;

    private String model;

    private String unit;

    private String materialType;

    private Integer isCollectionPurchase;

    private Integer collectionType;

    private String contractNo;

    private String udiCode;

    private String registrationNo;

    private LocalDate registrationValidDate;

    private Integer isHighValue;

    private Integer materialStatus;

    private String tenantId;

    @TableLogic
    private Integer delFlag;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;
}
