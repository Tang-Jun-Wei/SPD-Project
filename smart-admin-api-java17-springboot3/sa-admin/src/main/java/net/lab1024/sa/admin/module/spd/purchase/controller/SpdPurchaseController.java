package net.lab1024.sa.admin.module.spd.purchase.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.lab1024.sa.admin.module.spd.purchase.domain.form.SpdPurchaseForm;
import net.lab1024.sa.admin.module.spd.purchase.domain.form.SpdPurchaseQueryForm;
import net.lab1024.sa.admin.module.spd.purchase.domain.vo.SpdPurchaseVO;
import net.lab1024.sa.admin.module.spd.purchase.service.SpdPurchaseService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@Tag(name = "SPD采购管理")
@RestController
@RequestMapping("/spd/purchase")
public class SpdPurchaseController {
    @Autowired
    private SpdPurchaseService spdPurchaseService;

    @Operation(summary = "分页查询采购单")
    @PostMapping("/queryPage")
    @SaCheckPermission("spd:purchase:query")
    public ResponseDTO<PageResult<SpdPurchaseVO>> queryPage(@RequestBody @Valid SpdPurchaseQueryForm queryForm) {
        return ResponseDTO.ok(spdPurchaseService.queryPage(queryForm));
    }

    @Operation(summary = "查询采购单详情")
    @GetMapping("/detail/{id}")
    @SaCheckPermission("spd:purchase:detail")
    public ResponseDTO<SpdPurchaseVO> getDetail(@PathVariable Long id) {
        return spdPurchaseService.getDetail(id);
    }

    @Operation(summary = "新增采购单")
    @PostMapping("/add")
    @SaCheckPermission("spd:purchase:add")
    public ResponseDTO<String> add(@RequestBody @Valid SpdPurchaseForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdPurchaseService.add(form, requestUser.getUserId().toString(), "default");
    }

    @Operation(summary = "删除采购单")
    @GetMapping("/delete/{id}")
    @SaCheckPermission("spd:purchase:delete")
    public ResponseDTO<String> delete(@PathVariable Long id) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdPurchaseService.delete(id, requestUser.getUserId().toString());
    }
}
