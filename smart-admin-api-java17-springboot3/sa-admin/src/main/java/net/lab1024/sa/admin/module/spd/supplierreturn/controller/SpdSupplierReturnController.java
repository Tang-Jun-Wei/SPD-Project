package net.lab1024.sa.admin.module.spd.supplierreturn.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import net.lab1024.sa.admin.module.spd.supplierreturn.domain.form.SpdSupplierReturnForm;
import net.lab1024.sa.admin.module.spd.supplierreturn.domain.vo.SpdSupplierReturnVO;
import net.lab1024.sa.admin.module.spd.supplierreturn.service.SpdSupplierReturnService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "退供应商管理")
@RequestMapping("/spd/supplier-return")
public class SpdSupplierReturnController {

    @Resource
    private SpdSupplierReturnService spdSupplierReturnService;

    /**
     * 分页查询退供应商单
     */
    @Operation(summary = "分页查询退供应商单")
    @PostMapping("/queryPage")
    public ResponseDTO<PageResult<SpdSupplierReturnVO>> queryPage(@RequestBody Object form) {
        return spdSupplierReturnService.queryPage(form);
    }

    /**
     * 查询退供应商单详情
     */
    @Operation(summary = "查询退供应商单详情")
    @GetMapping("/getDetail/{supplierReturnId}")
    public ResponseDTO<SpdSupplierReturnVO> getDetail(@PathVariable String supplierReturnId) {
        return spdSupplierReturnService.getDetail(supplierReturnId);
    }

    /**
     * 新增退供应商单
     */
    @Operation(summary = "新增退供应商单")
    @PostMapping("/add")
    public ResponseDTO<String> add(@RequestBody SpdSupplierReturnForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdSupplierReturnService.add(form, requestUser.getUserId().toString(), "default");
    }

    /**
     * 审核退供应商单
     */
    @Operation(summary = "审核退供应商单")
    @PostMapping("/audit")
    public ResponseDTO<String> audit(@RequestParam String supplierReturnId, 
                                     @RequestParam Integer auditStatus,
                                     @RequestParam(required = false) String auditOpinion) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdSupplierReturnService.audit(supplierReturnId, auditStatus, auditOpinion, requestUser.getUserId().toString());
    }

    /**
     * 删除退供应商单
     */
    @Operation(summary = "删除退供应商单")
    @GetMapping("/delete/{supplierReturnId}")
    public ResponseDTO<String> delete(@PathVariable String supplierReturnId) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdSupplierReturnService.delete(supplierReturnId, requestUser.getUserId().toString());
    }
}
