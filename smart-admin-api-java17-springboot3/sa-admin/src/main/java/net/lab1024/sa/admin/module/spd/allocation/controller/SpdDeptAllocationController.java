package net.lab1024.sa.admin.module.spd.allocation.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.lab1024.sa.admin.module.spd.allocation.domain.form.SpdDeptAllocationForm;
import net.lab1024.sa.admin.module.spd.allocation.domain.vo.SpdDeptAllocationVO;
import net.lab1024.sa.admin.module.spd.allocation.service.SpdDeptAllocationService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

@RestController
@Tag(name = "科室调拨管理")
@RequestMapping("/spd/allocation")
public class SpdDeptAllocationController {

    @Resource
    private SpdDeptAllocationService spdDeptAllocationService;

    @Operation(summary = "分页查询调拨单")
    @PostMapping("/queryPage")
    @SaCheckPermission("spd:allocation:query")
    public ResponseDTO<PageResult<SpdDeptAllocationVO>> queryPage(@RequestBody Object form) {
        return spdDeptAllocationService.queryPage(form);
    }

    @Operation(summary = "查询调拨单详情")
    @GetMapping("/getDetail/{allocationId}")
    @SaCheckPermission("spd:allocation:query")
    public ResponseDTO<SpdDeptAllocationVO> getDetail(@PathVariable String allocationId) {
        return spdDeptAllocationService.getDetail(allocationId);
    }

    @Operation(summary = "新增调拨单")
    @PostMapping("/add")
    @SaCheckPermission("spd:allocation:add")
    public ResponseDTO<String> add(@Valid @RequestBody SpdDeptAllocationForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdDeptAllocationService.add(form, requestUser.getUserId().toString(), requestUser.getUserId().toString());
    }

    @Operation(summary = "审核调拨单")
    @PostMapping("/audit")
    @SaCheckPermission("spd:allocation:audit")
    public ResponseDTO<String> audit(@RequestParam String allocationId, @RequestParam Integer auditStatus, @RequestParam(required = false) String auditOpinion) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdDeptAllocationService.audit(allocationId, auditStatus, auditOpinion, requestUser.getUserId().toString());
    }

    @Operation(summary = "删除调拨单")
    @GetMapping("/delete/{allocationId}")
    @SaCheckPermission("spd:allocation:delete")
    public ResponseDTO<String> delete(@PathVariable String allocationId) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdDeptAllocationService.delete(allocationId, requestUser.getUserId().toString());
    }
}
