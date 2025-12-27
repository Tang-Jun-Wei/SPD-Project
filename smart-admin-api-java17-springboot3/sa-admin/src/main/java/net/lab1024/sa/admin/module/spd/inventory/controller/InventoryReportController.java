package net.lab1024.sa.admin.module.spd.inventory.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import net.lab1024.sa.admin.module.spd.inventory.domain.form.InventoryQueryForm;
import net.lab1024.sa.admin.module.spd.inventory.domain.vo.InventoryOverviewVO;
import net.lab1024.sa.admin.module.spd.inventory.domain.vo.InventorySummaryVO;
import net.lab1024.sa.admin.module.spd.inventory.service.InventoryReportService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 库存报表Controller
 */
@RestController
@Tag(name = "库存报表")
@RequestMapping("/spd/inventoryReport")
public class InventoryReportController {

    @Autowired(required = false)
    private InventoryReportService inventoryReportService;

    @Operation(summary = "库存统计概览")
    @GetMapping("/overview")
    public ResponseDTO<Map<String, Object>> getOverview() {
        if (inventoryReportService == null) {
            // 返回模拟数据
            Map<String, Object> overview = new HashMap<>();
            overview.put("totalValue", 0);
            overview.put("totalQuantity", 0);
            overview.put("warningCount", 0);
            overview.put("expiryWarningCount", 0);
            return ResponseDTO.ok(overview);
        }
        return inventoryReportService.getOverview();
    }

    @Operation(summary = "库存明细列表")
    @PostMapping("/queryDetail")
    public ResponseDTO<PageResult<InventorySummaryVO>> queryInventoryDetail(@RequestBody @Valid InventoryQueryForm queryForm) {
        if (inventoryReportService == null) {
            PageResult<InventorySummaryVO> emptyResult = new PageResult<>();
            emptyResult.setTotal(0L);
            emptyResult.setList(java.util.Collections.emptyList());
            emptyResult.setEmptyFlag(true);
            return ResponseDTO.ok(emptyResult);
        }
        return inventoryReportService.queryInventoryDetail(queryForm);
    }

    @Operation(summary = "库存预警列表")
    @PostMapping("/queryWarning")
    public ResponseDTO<PageResult<InventorySummaryVO>> queryWarning(@RequestBody @Valid InventoryQueryForm queryForm) {
        if (inventoryReportService == null) {
            PageResult<InventorySummaryVO> emptyResult = new PageResult<>();
            emptyResult.setTotal(0L);
            emptyResult.setList(java.util.Collections.emptyList());
            emptyResult.setEmptyFlag(true);
            return ResponseDTO.ok(emptyResult);
        }
        return inventoryReportService.queryWarning(queryForm);
    }

    @Operation(summary = "效期预警列表")
    @PostMapping("/queryExpiryWarning")
    public ResponseDTO<PageResult<InventorySummaryVO>> queryExpiryWarning(@RequestBody @Valid InventoryQueryForm queryForm) {
        if (inventoryReportService == null) {
            PageResult<InventorySummaryVO> emptyResult = new PageResult<>();
            emptyResult.setTotal(0L);
            emptyResult.setList(java.util.Collections.emptyList());
            emptyResult.setEmptyFlag(true);
            return ResponseDTO.ok(emptyResult);
        }
        return inventoryReportService.queryExpiryWarning(queryForm);
    }
}
