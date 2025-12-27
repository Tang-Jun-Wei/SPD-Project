package net.lab1024.sa.admin.module.spd.acceptance.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import net.lab1024.sa.admin.module.spd.acceptance.domain.form.SpdAcceptanceForm;
import net.lab1024.sa.admin.module.spd.acceptance.service.SpdAcceptanceService;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "SPD验收管理")
public class SpdAcceptanceController {

    @Autowired
    private SpdAcceptanceService spdAcceptanceService;

    @Operation(summary = "新增验收单")
    @PostMapping("/spd/acceptance/add")
    @SaCheckPermission("spd:acceptance:add")
    public ResponseDTO<String> add(@RequestBody @Valid SpdAcceptanceForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdAcceptanceService.add(form, requestUser.getUserId().toString(), "default");
    }
}
