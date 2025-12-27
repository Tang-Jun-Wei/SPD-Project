package net.lab1024.sa.admin.module.spd.manufacturer.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Schema(description = "生产厂家VO")
public class SpdManufacturerVO {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "厂家业务ID")
    private String manufacturerId;

    @Schema(description = "厂家名称")
    private String manufacturerName;

    @Schema(description = "联系人")
    private String contactPerson;

    @Schema(description = "联系电话")
    private String contactPhone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "生产许可证号")
    private String licenseNo;

    @Schema(description = "许可证有效期")
    private LocalDate licenseExpiry;

    @Schema(description = "状态:1-启用,0-禁用")
    private Integer status;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
