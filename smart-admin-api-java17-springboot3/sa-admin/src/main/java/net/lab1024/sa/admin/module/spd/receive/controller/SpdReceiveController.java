package net.lab1024.sa.admin.module.spd.receive.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.lab1024.sa.admin.module.spd.receive.domain.form.SpdReceiveForm;
import net.lab1024.sa.admin.module.spd.receive.domain.form.SpdReceiveQueryForm;
import net.lab1024.sa.admin.module.spd.receive.domain.vo.SpdReceiveVO;
import net.lab1024.sa.admin.module.spd.receive.service.SpdReceiveService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/spd/receive")
@Tag(name = "SPD收货管理")
public class SpdReceiveController {

    @Autowired
    private SpdReceiveService spdReceiveService;

    @Operation(summary = "分页查询")
    @PostMapping("/queryPage")
    @SaCheckPermission("spd:receive:query")
    public ResponseDTO<PageResult<SpdReceiveVO>> queryPage(@RequestBody @Valid SpdReceiveQueryForm queryForm) {
        return ResponseDTO.ok(spdReceiveService.queryPage(queryForm));
    }

    @Operation(summary = "查询详情")
    @GetMapping("/detail/{receiveCode}")
    @SaCheckPermission("spd:receive:query")
    public ResponseDTO<SpdReceiveVO> getDetail(@PathVariable String receiveCode) {
        return spdReceiveService.getDetail(receiveCode);
    }

    @Operation(summary = "新增收货单")
    @PostMapping("/add")
    @SaCheckPermission("spd:receive:add")
    public ResponseDTO<String> add(@RequestBody @Valid SpdReceiveForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdReceiveService.add(form, requestUser.getUserId().toString(), "default");
    }

    @Operation(summary = "更新收货单")
    @PostMapping("/update")
    @SaCheckPermission("spd:receive:update")
    public ResponseDTO<String> update(@RequestBody @Valid SpdReceiveForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdReceiveService.update(form, requestUser.getUserId().toString());
    }

    @Operation(summary = "删除收货单")
    @GetMapping("/delete/{receiveCode}")
    @SaCheckPermission("spd:receive:delete")
    public ResponseDTO<String> delete(@PathVariable String receiveCode) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdReceiveService.delete(receiveCode, requestUser.getUserId().toString());
    }

    @Operation(summary = "快捷收货")
    @PostMapping("/do")
    @SaCheckPermission("spd:receive:add")
    public ResponseDTO<String> receive(@RequestParam String acceptanceCode, @RequestParam String warehouseId) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdReceiveService.receive(acceptanceCode, warehouseId, requestUser.getUserId().toString(), "default");
    }
}
