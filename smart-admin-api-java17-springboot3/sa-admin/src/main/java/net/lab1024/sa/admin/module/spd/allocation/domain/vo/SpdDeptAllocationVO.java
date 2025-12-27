package net.lab1024.sa.admin.module.spd.allocation.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "科室调拨VO")
public class SpdDeptAllocationVO {
    private Long id;
    private String allocationId;
    private String allocationCode;
    private String fromNodeId;
    private String fromNodeName;
    private String toNodeId;
    private String toNodeName;
    private Integer allocationStatus;
    private String applyUserId;
    private String applyUserName;
    private LocalDateTime applyTime;
    private String auditUserId;
    private String auditUserName;
    private LocalDateTime auditTime;
    private String auditOpinion;
    private String remark;
    private LocalDateTime createTime;
    
    private List<DetailVO> detailList;
    
    @Data
    @Schema(description = "调拨明细VO")
    public static class DetailVO {
        private Long id;
        private String materialId;
        private String materialName;
        private String batchId;
        private String batchNo;
        private Integer allocationQuantity;
        private Integer actualQuantity;
        private String remark;
    }
}
