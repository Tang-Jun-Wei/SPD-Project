package net.lab1024.sa.admin.module.spd.acceptance.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.lab1024.sa.admin.constant.AdminSwaggerTagConst;
import net.lab1024.sa.admin.module.spd.acceptance.domain.form.SpdAcceptanceForm;
import net.lab1024.sa.admin.module.spd.acceptance.domain.form.SpdAcceptanceQueryForm;
import net.lab1024.sa.admin.module.spd.acceptance.domain.vo.SpdAcceptanceVO;
import net.lab1024.sa.admin.module.spd.acceptance.service.SpdAcceptanceService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.validation.Valid;

@RestController
@Tag(name = "SPD验收管理")
@RequestMapping("/spd/acceptance")
public class SpdAcceptanceController {

    @Autowired
    private SpdAcceptanceService spdAcceptanceService;

    @Operation(summary = "分页查询")
    @PostMapping("/queryPage")
    @SaCheckPermission("spd:acceptance:query")
    public ResponseDTO<PageResult<SpdAcceptanceVO>> queryPage(@RequestBody @Valid SpdAcceptanceQueryForm queryForm) {
        return ResponseDTO.ok(spdAcceptanceService.queryPage(queryForm));
    }

    @Operation(summary = "查询详情")
    @GetMapping("/queryDetail/{acceptanceId}")
    @SaCheckPermission("spd:acceptance:query")
    public ResponseDTO<SpdAcceptanceVO> queryDetail(@PathVariable String acceptanceId) {
        return spdAcceptanceService.queryDetail(acceptanceId);
    }

    @Operation(summary = "新增")
    @PostMapping("/add")
    @SaCheckPermission("spd:acceptance:add")
    public ResponseDTO<String> add(@RequestBody @Valid SpdAcceptanceForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdAcceptanceService.add(form, requestUser.getUserId().toString(), "default");
    }

    @Operation(summary = "更新")
    @PostMapping("/update")
    @SaCheckPermission("spd:acceptance:update")
    public ResponseDTO<String> update(@RequestBody @Valid SpdAcceptanceForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdAcceptanceService.update(form, requestUser.getUserId().toString());
    }

    @Operation(summary = "删除")
    @GetMapping("/delete/{acceptanceId}")
    @SaCheckPermission("spd:acceptance:delete")
    public ResponseDTO<String> delete(@PathVariable String acceptanceId) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdAcceptanceService.delete(acceptanceId, requestUser.getUserId().toString());
    }
}
