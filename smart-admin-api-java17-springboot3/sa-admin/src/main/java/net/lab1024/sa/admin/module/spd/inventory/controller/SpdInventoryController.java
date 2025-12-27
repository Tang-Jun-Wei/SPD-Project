package net.lab1024.sa.admin.module.spd.inventory.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import net.lab1024.sa.admin.module.spd.inventory.domain.form.SpdInventoryQueryForm;
import net.lab1024.sa.admin.module.spd.inventory.domain.vo.SpdInventoryVO;
import net.lab1024.sa.admin.module.spd.inventory.service.SpdInventoryService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "SPD库存管理")
public class SpdInventoryController {

    @Autowired
    private SpdInventoryService spdInventoryService;

    @Operation(summary = "分页查询库存")
    @PostMapping("/spd/inventory/query")
    @SaCheckPermission("spd:inventory:query")
    public ResponseDTO<PageResult<SpdInventoryVO>> queryPage(@RequestBody @Valid SpdInventoryQueryForm queryForm) {
        return spdInventoryService.queryPage(queryForm);
    }

    @Operation(summary = "查询库存详情")
    @GetMapping("/spd/inventory/get/{id}")
    @SaCheckPermission("spd:inventory:detail")
    public ResponseDTO<SpdInventoryVO> getDetail(@PathVariable Long id) {
        return spdInventoryService.getDetail(id);
    }
}
