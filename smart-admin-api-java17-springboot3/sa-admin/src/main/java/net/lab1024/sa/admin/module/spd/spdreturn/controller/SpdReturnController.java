package net.lab1024.sa.admin.module.spd.spdreturn.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.lab1024.sa.admin.module.spd.spdreturn.service.SpdReturnService;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "SPD退库管理")
public class SpdReturnController {

    @Autowired
    private SpdReturnService spdReturnService;

    @Operation(summary = "退库")
    @PostMapping("/spd/return/do")
    @SaCheckPermission("spd:return:add")
    public ResponseDTO<String> returnMaterial(@RequestParam String warehouseId, @RequestParam String materialId,
                                             @RequestParam String batchId, @RequestParam Integer returnNum) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdReturnService.returnMaterial(warehouseId, materialId, batchId, returnNum, requestUser.getUserId().toString(), "default");
    }
}
