package net.lab1024.sa.admin.module.spd.deptcategory.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "科室分类树形VO")
public class SpdDeptCategoryTreeVO {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "分类业务ID")
    private String categoryId;

    @Schema(description = "分类名称")
    private String categoryName;

    @Schema(description = "分类编码")
    private String categoryCode;

    @Schema(description = "上级分类ID")
    private String parentId;

    @Schema(description = "分类层级")
    private Integer categoryLevel;

    @Schema(description = "排序号")
    private Integer sortOrder;

    @Schema(description = "状态: 1=启用 0=禁用")
    private Integer categoryStatus;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "子分类列表")
    private List<SpdDeptCategoryTreeVO> children;

    @Schema(description = "是否有子节点")
    private Boolean hasChildren;
}
