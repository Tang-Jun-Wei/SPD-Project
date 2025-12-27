package net.lab1024.sa.admin.module.spd.location.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.lab1024.sa.admin.constant.AdminSwaggerTagConst;
import net.lab1024.sa.admin.module.spd.location.domain.form.SpdLocationForm;
import net.lab1024.sa.admin.module.spd.location.domain.vo.SpdLocationVO;
import net.lab1024.sa.admin.module.spd.location.service.SpdLocationService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

@RestController
@Tag(name = "货位管理")
@RequestMapping("/spd/location")
public class SpdLocationController {

    @Resource
    private SpdLocationService spdLocationService;

    @Operation(summary = "分页查询货位")
    @PostMapping("/queryPage")
    @SaCheckPermission("spd:location:query")
    public ResponseDTO<PageResult<SpdLocationVO>> queryPage(@RequestBody Object form) {
        return spdLocationService.queryPage(form);
    }

    @Operation(summary = "新增货位")
    @PostMapping("/add")
    @SaCheckPermission("spd:location:add")
    public ResponseDTO<String> add(@Valid @RequestBody SpdLocationForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdLocationService.add(form, requestUser.getUserId().toString(), requestUser.getUserId().toString());
    }

    @Operation(summary = "更新货位")
    @PostMapping("/update")
    @SaCheckPermission("spd:location:update")
    public ResponseDTO<String> update(@Valid @RequestBody SpdLocationForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdLocationService.update(form, requestUser.getUserId().toString());
    }

    @Operation(summary = "删除货位")
    @GetMapping("/delete/{locationId}")
    @SaCheckPermission("spd:location:delete")
    public ResponseDTO<String> delete(@PathVariable String locationId) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdLocationService.delete(locationId, requestUser.getUserId().toString());
    }

    @Operation(summary = "更新货位状态")
    @PostMapping("/updateStatus")
    @SaCheckPermission("spd:location:updateStatus")
    public ResponseDTO<String> updateStatus(@RequestParam String locationId, @RequestParam Integer status) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdLocationService.updateStatus(locationId, status, requestUser.getUserId().toString());
    }
}
