package net.lab1024.sa.admin.module.spd.location.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "货位VO")
public class SpdLocationVO {
    private Long id;
    private String locationId;
    private String locationCode;
    private String locationName;
    private String nodeId;
    private String nodeName;
    private Integer locationLevel;
    private Integer locationType;
    private Integer maxCapacity;
    private Integer currentQuantity;
    private Integer locationStatus;
    private String remark;
    private LocalDateTime createTime;
}
