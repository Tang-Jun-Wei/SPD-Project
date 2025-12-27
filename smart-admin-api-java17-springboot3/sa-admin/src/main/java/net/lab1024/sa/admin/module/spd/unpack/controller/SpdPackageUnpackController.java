package net.lab1024.sa.admin.module.spd.unpack.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.lab1024.sa.admin.module.spd.unpack.domain.form.SpdPackageUnpackAddForm;
import net.lab1024.sa.admin.module.spd.unpack.domain.form.SpdPackageUnpackQueryForm;
import net.lab1024.sa.admin.module.spd.unpack.domain.vo.SpdPackageUnpackVO;
import net.lab1024.sa.admin.module.spd.unpack.service.SpdPackageUnpackService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "定数包拆包管理")
@RestController
@RequestMapping("/spd/package-unpack")
public class SpdPackageUnpackController {

    @Autowired
    private SpdPackageUnpackService spdPackageUnpackService;

    @Operation(summary = "分页查询")
    @PostMapping("/queryPage")
    @SaCheckPermission("spd:unpack:query")
    public ResponseDTO<PageResult<SpdPackageUnpackVO>> queryPage(@RequestBody SpdPackageUnpackQueryForm queryForm) {
        return spdPackageUnpackService.queryPage(queryForm);
    }

    @Operation(summary = "查询详情")
    @GetMapping("/getDetail/{unpackId}")
    @SaCheckPermission("spd:unpack:query")
    public ResponseDTO<SpdPackageUnpackVO> getDetail(@PathVariable Long unpackId) {
        return spdPackageUnpackService.getDetail(unpackId);
    }

    @Operation(summary = "新增拆包")
    @PostMapping("/add")
    @SaCheckPermission("spd:unpack:add")
    public ResponseDTO<String> add(@RequestBody SpdPackageUnpackAddForm addForm) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdPackageUnpackService.add(addForm, requestUser.getUserId(), requestUser.getUserName());
    }

    @Operation(summary = "删除")
    @GetMapping("/delete/{unpackId}")
    @SaCheckPermission("spd:unpack:delete")
    public ResponseDTO<String> delete(@PathVariable Long unpackId) {
        return spdPackageUnpackService.delete(unpackId);
    }
}
