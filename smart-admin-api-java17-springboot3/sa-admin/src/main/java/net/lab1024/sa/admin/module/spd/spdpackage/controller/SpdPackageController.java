package net.lab1024.sa.admin.module.spd.spdpackage.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.lab1024.sa.admin.module.spd.spdpackage.service.SpdPackageService;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "SPD打包管理")
public class SpdPackageController {

    @Autowired
    private SpdPackageService spdPackageService;

    @Operation(summary = "创建打包单")
    @PostMapping("/spd/package/create")
    @SaCheckPermission("spd:package:add")
    public ResponseDTO<String> createPackage(@RequestParam String warehouseId) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdPackageService.createPackage(warehouseId, requestUser.getUserId().toString(), "default");
    }
}
