package net.lab1024.sa.admin.module.spd.deptcategory.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.lab1024.sa.admin.constant.AdminSwaggerTagConst;
import net.lab1024.sa.admin.module.spd.deptcategory.domain.form.SpdDeptCategoryForm;
import net.lab1024.sa.admin.module.spd.deptcategory.domain.vo.SpdDeptCategoryTreeVO;
import net.lab1024.sa.admin.module.spd.deptcategory.service.SpdDeptCategoryService;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@Tag(name = AdminSwaggerTagConst.SPD.SPD_DEPT_CATEGORY)
@RequestMapping("/spd/deptCategory")
public class SpdDeptCategoryController {

    @Resource
    private SpdDeptCategoryService spdDeptCategoryService;

    @Operation(summary = "查询科室分类树")
    @GetMapping("/queryTree")
    @SaCheckPermission("spd:deptCategory:query")
    public ResponseDTO<List<SpdDeptCategoryTreeVO>> queryTree() {
        return spdDeptCategoryService.queryTree("default");
    }

    @Operation(summary = "查询所有科室分类")
    @GetMapping("/queryAll")
    @SaCheckPermission("spd:deptCategory:query")
    public ResponseDTO<List<SpdDeptCategoryTreeVO>> queryAll() {
        return spdDeptCategoryService.queryAll("default");
    }

    @Operation(summary = "新增科室分类")
    @PostMapping("/add")
    @SaCheckPermission("spd:deptCategory:add")
    public ResponseDTO<String> add(@Valid @RequestBody SpdDeptCategoryForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdDeptCategoryService.add(form, requestUser.getUserId().toString(), "default");
    }

    @Operation(summary = "更新科室分类")
    @PostMapping("/update")
    @SaCheckPermission("spd:deptCategory:update")
    public ResponseDTO<String> update(@Valid @RequestBody SpdDeptCategoryForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdDeptCategoryService.update(form, requestUser.getUserId().toString());
    }

    @Operation(summary = "删除科室分类")
    @GetMapping("/delete/{categoryId}")
    @SaCheckPermission("spd:deptCategory:delete")
    public ResponseDTO<String> delete(@PathVariable String categoryId) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdDeptCategoryService.delete(categoryId, requestUser.getUserId().toString());
    }

    @Operation(summary = "更新科室分类状态")
    @PostMapping("/updateStatus")
    @SaCheckPermission("spd:deptCategory:updateStatus")
    public ResponseDTO<String> updateStatus(@RequestParam String categoryId, @RequestParam Integer status) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdDeptCategoryService.updateStatus(categoryId, status, requestUser.getUserId().toString());
    }
}
