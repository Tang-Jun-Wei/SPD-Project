package net.lab1024.sa.admin.module.spd.unit.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import net.lab1024.sa.admin.module.spd.unit.domain.form.SpdUnitConversionForm;
import net.lab1024.sa.admin.module.spd.unit.domain.form.SpdUnitConversionQueryForm;
import net.lab1024.sa.admin.module.spd.unit.domain.vo.SpdUnitConversionVO;
import net.lab1024.sa.admin.module.spd.unit.service.SpdUnitConversionService;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "SPD单位换算管理")
@RestController
@RequestMapping("/spd/unit/conversion")
public class SpdUnitConversionController {

    @Autowired
    private SpdUnitConversionService conversionService;

    @Operation(summary = "查询换算关系列表")
    @PostMapping("/queryList")
    @SaCheckPermission("spd:unit:conversion:query")
    public ResponseDTO<List<SpdUnitConversionVO>> queryList(@RequestBody SpdUnitConversionQueryForm queryForm) {
        return conversionService.queryList(queryForm);
    }

    @Operation(summary = "新增换算关系")
    @PostMapping("/add")
    @SaCheckPermission("spd:unit:conversion:add")
    public ResponseDTO<String> add(@RequestBody @Valid SpdUnitConversionForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return conversionService.add(form, requestUser.getUserId());
    }

    @Operation(summary = "修改换算关系")
    @PostMapping("/update")
    @SaCheckPermission("spd:unit:conversion:update")
    public ResponseDTO<String> update(@RequestBody @Valid SpdUnitConversionForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return conversionService.update(form, requestUser.getUserId());
    }

    @Operation(summary = "删除换算关系")
    @GetMapping("/delete/{id}")
    @SaCheckPermission("spd:unit:conversion:delete")
    public ResponseDTO<String> delete(@PathVariable Long id) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return conversionService.delete(id, requestUser.getUserId());
    }
}
