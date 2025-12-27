package net.lab1024.sa.admin.module.spd.manufacturer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.lab1024.sa.admin.module.spd.manufacturer.domain.form.SpdManufacturerForm;
import net.lab1024.sa.admin.module.spd.manufacturer.domain.form.SpdManufacturerQueryForm;
import net.lab1024.sa.admin.module.spd.manufacturer.domain.vo.SpdManufacturerVO;
import net.lab1024.sa.admin.module.spd.manufacturer.service.SpdManufacturerService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@Tag(name = "SPD-生产厂家管理")
@RequestMapping("/spd/manufacturer")
public class SpdManufacturerController {

    @Resource
    private SpdManufacturerService spdManufacturerService;

    @Operation(summary = "分页查询")
    @PostMapping("/queryPage")
    public ResponseDTO<PageResult<SpdManufacturerVO>> queryPage(@RequestBody @Valid SpdManufacturerQueryForm queryForm) {
        return spdManufacturerService.queryPage(queryForm);
    }

    @Operation(summary = "查询详情")
    @GetMapping("/detail/{id}")
    public ResponseDTO<SpdManufacturerVO> getDetail(@PathVariable Long id) {
        return spdManufacturerService.getDetail(id);
    }

    @Operation(summary = "查询全部(下拉选项)")
    @GetMapping("/queryAll")
    public ResponseDTO<List<SpdManufacturerVO>> queryAll() {
        return spdManufacturerService.queryAll();
    }

    @Operation(summary = "新增")
    @PostMapping("/add")
    public ResponseDTO<String> add(@RequestBody @Valid SpdManufacturerForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdManufacturerService.add(form, requestUser.getUserId().toString(), "default");
    }

    @Operation(summary = "更新")
    @PostMapping("/update")
    public ResponseDTO<String> update(@RequestBody @Valid SpdManufacturerForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdManufacturerService.update(form, requestUser.getUserId().toString());
    }

    @Operation(summary = "删除")
    @GetMapping("/delete/{id}")
    public ResponseDTO<String> delete(@PathVariable Long id) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdManufacturerService.delete(id, requestUser.getUserId().toString());
    }

    @Operation(summary = "更新状态")
    @PostMapping("/updateStatus")
    public ResponseDTO<String> updateStatus(@RequestParam Long id, @RequestParam Integer status) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdManufacturerService.updateStatus(id, status, requestUser.getUserId().toString());
    }
}
