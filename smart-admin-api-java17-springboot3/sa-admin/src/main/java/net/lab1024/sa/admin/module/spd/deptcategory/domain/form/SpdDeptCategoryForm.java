package net.lab1024.sa.admin.module.spd.deptcategory.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
@Schema(description = "科室分类表单")
public class SpdDeptCategoryForm {

    @Schema(description = "分类ID(编辑时必填)")
    private String categoryId;

    @Schema(description = "分类名称", required = true)
    @NotBlank(message = "分类名称不能为空")
    private String categoryName;

    @Schema(description = "分类编码")
    private String categoryCode;

    @Schema(description = "上级分类ID(根节点可为空)")
    private String parentId;

    @Schema(description = "分类层级")
    private Integer categoryLevel;

    @Schema(description = "排序号")
    private Integer sortOrder;

    @Schema(description = "状态: 1=启用 0=禁用")
    private Integer categoryStatus;

    @Schema(description = "备注")
    private String remark;
}
