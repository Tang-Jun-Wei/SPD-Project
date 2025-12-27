package net.lab1024.sa.admin.module.spd.material.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;

/**
 * 耗材信息查询表单
 */
@Data
@Schema(description = "耗材信息查询表单")
public class SpdMaterialQueryForm extends PageParam {

    @Schema(description = "耗材名称")
    private String materialName;

    @Schema(description = "耗材编码")
    private String materialCode;

    @Schema(description = "生产厂家")
    private String manufacturer;

    @Schema(description = "供应商名称")
    private String supplierName;

    @Schema(description = "是否集采（0-否，1-是）")
    private Integer isGroupBuy;

    @Schema(description = "耗材分类")
    private String category;

    @Schema(description = "耗材状态（0-停用，1-启用）")
    private Integer materialStatus;
}
