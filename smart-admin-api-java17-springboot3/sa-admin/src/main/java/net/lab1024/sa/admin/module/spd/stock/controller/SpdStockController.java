package net.lab1024.sa.admin.module.spd.stock.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.lab1024.sa.admin.module.spd.stock.domain.form.SpdStockQueryForm;
import net.lab1024.sa.admin.module.spd.stock.domain.vo.SpdStockVO;
import net.lab1024.sa.admin.module.spd.stock.service.SpdStockService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@Tag(name = "SPD库存管理")
@RestController
@RequestMapping("/spd/stock")
public class SpdStockController {
    @Autowired
    private SpdStockService spdStockService;

    @Operation(summary = "分页查询库存")
    @PostMapping("/queryPage")
    @SaCheckPermission("spd:stock:query")
    public ResponseDTO<PageResult<SpdStockVO>> queryPage(@RequestBody @Valid SpdStockQueryForm queryForm) {
        return ResponseDTO.ok(spdStockService.queryPage(queryForm));
    }
}
