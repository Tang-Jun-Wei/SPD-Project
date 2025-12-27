package net.lab1024.sa.admin.module.spd.receive.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.lab1024.sa.admin.module.spd.receive.service.SpdReceiveService;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "SPD收货管理")
public class SpdReceiveController {

    @Autowired
    private SpdReceiveService spdReceiveService;

    @Operation(summary = "收货")
    @PostMapping("/spd/receive/do")
    @SaCheckPermission("spd:receive:add")
    public ResponseDTO<String> receive(@RequestParam String acceptanceCode, @RequestParam String warehouseId) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdReceiveService.receive(acceptanceCode, warehouseId, requestUser.getUserId().toString(), "default");
    }
}
