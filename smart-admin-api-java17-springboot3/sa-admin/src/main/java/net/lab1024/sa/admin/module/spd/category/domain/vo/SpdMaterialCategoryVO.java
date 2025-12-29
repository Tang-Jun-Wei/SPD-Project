package net.lab1024.sa.admin.module.spd.category.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 耗材分类VO(支持树形结构)
 */
@Data
@Schema(description = "耗材分类")
public class SpdMaterialCategoryVO {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "分类编码")
    private String categoryCode;

    @Schema(description = "分类名称")
    private String categoryName;

    @Schema(description = "父分类ID")
    private Long parentId;

    @Schema(description = "层级")
    private Integer level;

    @Schema(description = "排序号")
    private Integer sort;

    @Schema(description = "状态(1=启用/0=停用)")
    private Integer status;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "子分类列表")
    private List<SpdMaterialCategoryVO> children;
}
