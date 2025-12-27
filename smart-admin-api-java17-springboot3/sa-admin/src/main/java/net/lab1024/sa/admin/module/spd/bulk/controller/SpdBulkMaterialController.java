package net.lab1024.sa.admin.module.spd.bulk.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.lab1024.sa.admin.module.spd.bulk.domain.form.SpdBulkMaterialForm;
import net.lab1024.sa.admin.module.spd.bulk.domain.form.SpdBulkMaterialQueryForm;
import net.lab1024.sa.admin.module.spd.bulk.domain.vo.SpdBulkMaterialVO;
import net.lab1024.sa.admin.module.spd.bulk.service.SpdBulkMaterialService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/spd/bulk")
@Tag(name = "SPD散货管理")
public class SpdBulkMaterialController {

    @Autowired
    private SpdBulkMaterialService spdBulkMaterialService;

    @Operation(summary = "分页查询")
    @PostMapping("/queryPage")
    @SaCheckPermission("spd:bulk:query")
    public ResponseDTO<PageResult<SpdBulkMaterialVO>> queryPage(@RequestBody @Valid SpdBulkMaterialQueryForm queryForm) {
        return ResponseDTO.ok(spdBulkMaterialService.queryPage(queryForm));
    }

    @Operation(summary = "新增散货")
    @PostMapping("/add")
    @SaCheckPermission("spd:bulk:add")
    public ResponseDTO<String> add(@RequestBody @Valid SpdBulkMaterialForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdBulkMaterialService.add(form, requestUser.getUserId().toString(), "default");
    }

    @Operation(summary = "散货消耗")
    @PostMapping("/consume/{bulkId}")
    @SaCheckPermission("spd:bulk:consume")
    public ResponseDTO<String> consume(@PathVariable String bulkId, @RequestParam Integer consumeNum) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdBulkMaterialService.consume(bulkId, consumeNum, requestUser.getUserId().toString());
    }
}
