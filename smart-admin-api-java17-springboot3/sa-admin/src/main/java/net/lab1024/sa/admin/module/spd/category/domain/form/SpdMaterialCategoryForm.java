package net.lab1024.sa.admin.module.spd.category.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 耗材分类表单
 */
@Data
@Schema(description = "耗材分类表单")
public class SpdMaterialCategoryForm {

    @Schema(description = "主键ID(修改时必填)")
    private Long id;

    @Schema(description = "分类编码")
    private String categoryCode;

    @NotBlank(message = "分类名称不能为空")
    @Schema(description = "分类名称")
    private String categoryName;

    @NotNull(message = "父分类ID不能为空")
    @Schema(description = "父分类ID(0=顶级分类)")
    private Long parentId;

    @Schema(description = "排序号")
    private Integer sort;

    @Schema(description = "状态(1=启用/0=停用)")
    private Integer status;

    @Schema(description = "备注")
    private String remark;
}
