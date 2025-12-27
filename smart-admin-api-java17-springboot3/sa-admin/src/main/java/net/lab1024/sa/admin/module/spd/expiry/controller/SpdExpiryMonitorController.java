package net.lab1024.sa.admin.module.spd.expiry.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import net.lab1024.sa.admin.module.spd.expiry.domain.form.SpdExpiryMonitorQueryForm;
import net.lab1024.sa.admin.module.spd.expiry.domain.vo.SpdExpiryMonitorVO;
import net.lab1024.sa.admin.module.spd.expiry.service.SpdExpiryMonitorService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "SPD库存效期监控")
@RequestMapping("/spd/expiryMonitor")
public class SpdExpiryMonitorController {

    @Resource
    private SpdExpiryMonitorService spdExpiryMonitorService;

    @Operation(summary = "分页查询库存效期监控数据")
    @PostMapping("/queryPage")
    public ResponseDTO<PageResult<SpdExpiryMonitorVO>> queryPage(@RequestBody @Valid SpdExpiryMonitorQueryForm queryForm) {
        return spdExpiryMonitorService.queryPage(queryForm);
    }

    @Operation(summary = "查询即将过期的库存（用于预警）")
    @GetMapping("/queryExpiringSoon")
    public ResponseDTO<List<SpdExpiryMonitorVO>> queryExpiringSoon(@RequestParam(required = false, defaultValue = "30") Integer days) {
        return spdExpiryMonitorService.queryExpiringSoon(days);
    }
}
