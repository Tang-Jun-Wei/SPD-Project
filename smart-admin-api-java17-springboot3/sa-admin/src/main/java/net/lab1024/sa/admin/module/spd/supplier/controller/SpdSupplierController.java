package net.lab1024.sa.admin.module.spd.supplier.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.lab1024.sa.admin.module.spd.supplier.domain.form.SpdSupplierForm;
import net.lab1024.sa.admin.module.spd.supplier.domain.form.SpdSupplierQueryForm;
import net.lab1024.sa.admin.module.spd.supplier.domain.vo.SpdSupplierVO;
import net.lab1024.sa.admin.module.spd.supplier.service.SpdSupplierService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@Tag(name = "SPD-供应商管理")
@RequestMapping("/spd/supplier")
public class SpdSupplierController {

    @Resource
    private SpdSupplierService spdSupplierService;

    @Operation(summary = "分页查询")
    @PostMapping("/queryPage")
    public ResponseDTO<PageResult<SpdSupplierVO>> queryPage(@RequestBody @Valid SpdSupplierQueryForm queryForm) {
        return spdSupplierService.queryPage(queryForm);
    }

    @Operation(summary = "查询详情")
    @GetMapping("/detail/{id}")
    public ResponseDTO<SpdSupplierVO> getDetail(@PathVariable Long id) {
        return spdSupplierService.getDetail(id);
    }

    @Operation(summary = "查询全部(下拉选项)")
    @GetMapping("/queryAll")
    public ResponseDTO<List<SpdSupplierVO>> queryAll() {
        return spdSupplierService.queryAll();
    }

    @Operation(summary = "新增")
    @PostMapping("/add")
    public ResponseDTO<String> add(@RequestBody @Valid SpdSupplierForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdSupplierService.add(form, requestUser.getUserId().toString(), "default");
    }

    @Operation(summary = "更新")
    @PostMapping("/update")
    public ResponseDTO<String> update(@RequestBody @Valid SpdSupplierForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdSupplierService.update(form, requestUser.getUserId().toString());
    }

    @Operation(summary = "删除")
    @GetMapping("/delete/{id}")
    public ResponseDTO<String> delete(@PathVariable Long id) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdSupplierService.delete(id, requestUser.getUserId().toString());
    }

    @Operation(summary = "更新状态")
    @PostMapping("/updateStatus/{id}")
    public ResponseDTO<String> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdSupplierService.updateStatus(id, status, requestUser.getUserId().toString());
    }
}
