package net.lab1024.sa.admin.module.spd.label.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.lab1024.sa.admin.module.spd.label.service.SpdLabelService;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "SPD标签管理")
public class SpdLabelController {

    @Autowired
    private SpdLabelService spdLabelService;

    @Operation(summary = "生成标签")
    @PostMapping("/spd/label/generate")
    @SaCheckPermission("spd:label:add")
    public ResponseDTO<String> generateLabel(@RequestParam String materialId, @RequestParam String batchId, @RequestParam String warehouseId) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdLabelService.generateLabel(materialId, batchId, warehouseId, requestUser.getUserId().toString(), "default");
    }
}
