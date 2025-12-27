package net.lab1024.sa.admin.module.spd.bulkpackage.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.lab1024.sa.admin.module.spd.bulkpackage.domain.form.SpdBulkPackageAddForm;
import net.lab1024.sa.admin.module.spd.bulkpackage.domain.form.SpdBulkPackageQueryForm;
import net.lab1024.sa.admin.module.spd.bulkpackage.domain.vo.SpdBulkPackageVO;
import net.lab1024.sa.admin.module.spd.bulkpackage.service.SpdBulkPackageService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 散货打包Controller
 */
@Tag(name = "散货打包管理")
@RestController
@RequestMapping("/spd/bulk-package")
public class SpdBulkPackageController {

    @Autowired
    private SpdBulkPackageService spdBulkPackageService;

    @Operation(summary = "分页查询")
    @PostMapping("/queryPage")
    @SaCheckPermission("spd:bulkPackage:query")
    public ResponseDTO<PageResult<SpdBulkPackageVO>> queryPage(@RequestBody SpdBulkPackageQueryForm queryForm) {
        return spdBulkPackageService.queryPage(queryForm);
    }

    @Operation(summary = "查询详情")
    @GetMapping("/getDetail/{packageId}")
    @SaCheckPermission("spd:bulkPackage:query")
    public ResponseDTO<SpdBulkPackageVO> getDetail(@PathVariable Long packageId) {
        return spdBulkPackageService.getDetail(packageId);
    }

    @Operation(summary = "新增打包")
    @PostMapping("/add")
    @SaCheckPermission("spd:bulkPackage:add")
    public ResponseDTO<String> add(@RequestBody SpdBulkPackageAddForm addForm) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdBulkPackageService.add(addForm, requestUser.getUserId(), requestUser.getUserName());
    }

    @Operation(summary = "删除")
    @GetMapping("/delete/{packageId}")
    @SaCheckPermission("spd:bulkPackage:delete")
    public ResponseDTO<String> delete(@PathVariable Long packageId) {
        return spdBulkPackageService.delete(packageId);
    }
}
