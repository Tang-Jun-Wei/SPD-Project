package net.lab1024.sa.admin.module.spd.batch.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Schema(description = "SPD批号VO")
public class SpdMaterialBatchVO {
    private Long id;
    private String batchId;
    private String materialId;
    private String materialName;
    private String batchNo;
    private LocalDate validDate;
    private String supplierId;
    private String supplierName;
    private LocalDateTime createTime;
}
