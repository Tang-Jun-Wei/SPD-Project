package net.lab1024.sa.admin.module.spd.apply.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 申领信息实体类
 */
@Data
@TableName("t_spd_apply")
public class SpdApplyEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String applyId;
    private String applyNo;
    private String applyDeptId;
    private String applyDeptName;
    private String applyUserId;
    private String applyUserName;
    private LocalDateTime applyTime;
    private Integer applyStatus;
    private String approveUserId;
    private String approveUserName;
    private LocalDateTime approveTime;
    private String approveRemark;
    private String tenantId;
    private Integer delFlag;
    private String createBy;
    private LocalDateTime createTime;
    private String updateBy;
    private LocalDateTime updateTime;
    private String remark;
}
