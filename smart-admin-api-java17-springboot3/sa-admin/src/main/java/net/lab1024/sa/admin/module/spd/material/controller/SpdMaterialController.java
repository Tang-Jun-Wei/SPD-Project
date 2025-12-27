package net.lab1024.sa.admin.module.spd.material.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.lab1024.sa.admin.module.spd.material.domain.form.SpdMaterialForm;
import net.lab1024.sa.admin.module.spd.material.domain.form.SpdMaterialQueryForm;
import net.lab1024.sa.admin.module.spd.material.domain.vo.SpdMaterialVO;
import net.lab1024.sa.admin.module.spd.material.service.SpdMaterialService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * 耗材信息Controller
 */
@Tag(name = "SPD耗材管理")
@RestController
@RequestMapping("/spd/material")
public class SpdMaterialController {

    @Autowired
    private SpdMaterialService spdMaterialService;

    @Operation(summary = "分页查询耗材")
    @PostMapping("/queryPage")
    @SaCheckPermission("spd:material:query")
    public ResponseDTO<PageResult<SpdMaterialVO>> queryPage(@RequestBody @Valid SpdMaterialQueryForm queryForm) {
        return ResponseDTO.ok(spdMaterialService.queryPage(queryForm));
    }

    @Operation(summary = "新增耗材")
    @PostMapping("/add")
    @SaCheckPermission("spd:material:add")
    public ResponseDTO<String> add(@RequestBody @Valid SpdMaterialForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        String tenantId = "default";
        return spdMaterialService.add(form, requestUser.getUserId().toString(), tenantId);
    }

    @Operation(summary = "修改耗材")
    @PostMapping("/update")
    @SaCheckPermission("spd:material:update")
    public ResponseDTO<String> update(@RequestBody @Valid SpdMaterialForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdMaterialService.update(form, requestUser.getUserId().toString());
    }

    @Operation(summary = "删除耗材")
    @GetMapping("/delete/{id}")
    @SaCheckPermission("spd:material:delete")
    public ResponseDTO<String> delete(@PathVariable Long id) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdMaterialService.delete(id, requestUser.getUserId().toString());
    }
}
