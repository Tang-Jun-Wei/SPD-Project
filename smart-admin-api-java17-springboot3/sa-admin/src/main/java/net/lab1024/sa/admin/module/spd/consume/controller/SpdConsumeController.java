package net.lab1024.sa.admin.module.spd.consume.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.lab1024.sa.admin.module.spd.consume.service.SpdConsumeService;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "SPD消耗管理")
public class SpdConsumeController {

    @Autowired
    private SpdConsumeService spdConsumeService;

    @Operation(summary = "消耗记录")
    @PostMapping("/spd/consume/record")
    @SaCheckPermission("spd:consume:add")
    public ResponseDTO<String> consume(@RequestParam String deptId, @RequestParam String materialId, 
                                      @RequestParam String batchId, @RequestParam Integer consumeNum) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdConsumeService.consume(deptId, materialId, batchId, consumeNum, requestUser.getUserId().toString(), "default");
    }
}
