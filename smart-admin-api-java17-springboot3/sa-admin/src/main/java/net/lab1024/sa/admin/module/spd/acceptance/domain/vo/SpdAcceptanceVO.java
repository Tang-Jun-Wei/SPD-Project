package net.lab1024.sa.admin.module.spd.acceptance.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "验收单VO")
public class SpdAcceptanceVO {
    private Long id;
    private String acceptanceId;
    private String acceptanceCode;
    private String purchaseCode;
    private String warehouseId;
    private String warehouseName;
    private Integer acceptanceStatus;
    private String acceptanceStatusName;
    private String createUser;
    private LocalDateTime createTime;
    private String remark;
}
