package net.lab1024.sa.admin.module.spd.spdpackage.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import net.lab1024.sa.admin.module.spd.spdpackage.domain.form.SpdPackageForm;
import net.lab1024.sa.admin.module.spd.spdpackage.domain.form.SpdPackageQueryForm;
import net.lab1024.sa.admin.module.spd.spdpackage.domain.vo.SpdPackageVO;
import net.lab1024.sa.admin.module.spd.spdpackage.service.SpdPackageService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "SPD打包管理")
@RequestMapping("/spd/package")
public class SpdPackageController {

    @Autowired
    private SpdPackageService spdPackageService;

    @Operation(summary = "分页查询")
    @PostMapping("/queryPage")
    @SaCheckPermission("spd:package:query")
    public ResponseDTO<PageResult<SpdPackageVO>> queryPage(@RequestBody @Valid SpdPackageQueryForm queryForm) {
        return ResponseDTO.ok(spdPackageService.queryPage(queryForm));
    }

    @Operation(summary = "查询详情")
    @GetMapping("/getDetail/{packageId}")
    @SaCheckPermission("spd:package:query")
    public ResponseDTO<SpdPackageVO> getDetail(@PathVariable String packageId) {
        return spdPackageService.getDetail(packageId);
    }

    @Operation(summary = "新增")
    @PostMapping("/add")
    @SaCheckPermission("spd:package:add")
    public ResponseDTO<String> add(@RequestBody @Valid SpdPackageForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdPackageService.add(form, requestUser.getUserId().toString(), "default");
    }

    @Operation(summary = "更新")
    @PostMapping("/update")
    @SaCheckPermission("spd:package:update")
    public ResponseDTO<String> update(@RequestBody @Valid SpdPackageForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdPackageService.update(form, requestUser.getUserId().toString());
    }

    @Operation(summary = "删除")
    @GetMapping("/delete/{packageId}")
    @SaCheckPermission("spd:package:delete")
    public ResponseDTO<String> delete(@PathVariable String packageId) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdPackageService.delete(packageId, requestUser.getUserId().toString());
    }

    @Operation(summary = "更新状态")
    @PostMapping("/updateStatus")
    @SaCheckPermission("spd:package:update")
    public ResponseDTO<String> updateStatus(@RequestParam String packageId, @RequestParam Integer packageStatus) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdPackageService.updateStatus(packageId, packageStatus, requestUser.getUserId().toString());
    }
}
