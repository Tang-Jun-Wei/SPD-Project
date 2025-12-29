package net.lab1024.sa.admin.module.spd.category.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import net.lab1024.sa.admin.module.spd.category.domain.form.SpdMaterialCategoryForm;
import net.lab1024.sa.admin.module.spd.category.domain.vo.SpdMaterialCategoryVO;
import net.lab1024.sa.admin.module.spd.category.service.SpdMaterialCategoryService;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 耗材分类Controller
 */
@Tag(name = "SPD耗材分类管理")
@RestController
@RequestMapping("/spd/category")
public class SpdMaterialCategoryController {

    @Autowired
    private SpdMaterialCategoryService categoryService;

    @Operation(summary = "查询分类树")
    @GetMapping("/queryTree")
    @SaCheckPermission("spd:category:query")
    public ResponseDTO<List<SpdMaterialCategoryVO>> queryTree() {
        return categoryService.queryTree();
    }

    @Operation(summary = "查询所有启用的分类(下拉选项)")
    @GetMapping("/queryAllEnabled")
    public ResponseDTO<List<SpdMaterialCategoryVO>> queryAllEnabled() {
        return categoryService.queryAllEnabled();
    }

    @Operation(summary = "新增分类")
    @PostMapping("/add")
    @SaCheckPermission("spd:category:add")
    public ResponseDTO<String> add(@RequestBody @Valid SpdMaterialCategoryForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return categoryService.add(form, requestUser.getUserId());
    }

    @Operation(summary = "修改分类")
    @PostMapping("/update")
    @SaCheckPermission("spd:category:update")
    public ResponseDTO<String> update(@RequestBody @Valid SpdMaterialCategoryForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return categoryService.update(form, requestUser.getUserId());
    }

    @Operation(summary = "删除分类")
    @GetMapping("/delete/{id}")
    @SaCheckPermission("spd:category:delete")
    public ResponseDTO<String> delete(@PathVariable Long id) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return categoryService.delete(id, requestUser.getUserId());
    }

    @Operation(summary = "更新状态")
    @PostMapping("/updateStatus")
    @SaCheckPermission("spd:category:updateStatus")
    public ResponseDTO<String> updateStatus(@RequestParam Long id, @RequestParam Integer status) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return categoryService.updateStatus(id, status, requestUser.getUserId());
    }
}
