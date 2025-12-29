package net.lab1024.sa.admin.module.spd.unit.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import net.lab1024.sa.admin.module.spd.unit.domain.form.SpdUnitForm;
import net.lab1024.sa.admin.module.spd.unit.domain.form.SpdUnitQueryForm;
import net.lab1024.sa.admin.module.spd.unit.domain.vo.SpdUnitVO;
import net.lab1024.sa.admin.module.spd.unit.service.SpdUnitService;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "SPD单位管理")
@RestController
@RequestMapping("/spd/unit")
public class SpdUnitController {

    @Autowired
    private SpdUnitService unitService;

    @Operation(summary = "查询单位列表")
    @PostMapping("/queryList")
    @SaCheckPermission("spd:unit:query")
    public ResponseDTO<List<SpdUnitVO>> queryList(@RequestBody SpdUnitQueryForm queryForm) {
        return unitService.queryPage(queryForm);
    }

    @Operation(summary = "查询所有启用的单位(下拉选项)")
    @GetMapping("/queryAll")
    public ResponseDTO<List<SpdUnitVO>> queryAll() {
        return unitService.queryAll();
    }

    @Operation(summary = "新增单位")
    @PostMapping("/add")
    @SaCheckPermission("spd:unit:add")
    public ResponseDTO<String> add(@RequestBody @Valid SpdUnitForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return unitService.add(form, requestUser.getUserId());
    }

    @Operation(summary = "修改单位")
    @PostMapping("/update")
    @SaCheckPermission("spd:unit:update")
    public ResponseDTO<String> update(@RequestBody @Valid SpdUnitForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return unitService.update(form, requestUser.getUserId());
    }

    @Operation(summary = "删除单位")
    @GetMapping("/delete/{id}")
    @SaCheckPermission("spd:unit:delete")
    public ResponseDTO<String> delete(@PathVariable Long id) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return unitService.delete(id, requestUser.getUserId());
    }

    @Operation(summary = "更新状态")
    @PostMapping("/updateStatus")
    @SaCheckPermission("spd:unit:updateStatus")
    public ResponseDTO<String> updateStatus(@RequestParam Long id, @RequestParam Integer status) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return unitService.updateStatus(id, status, requestUser.getUserId());
    }
}
