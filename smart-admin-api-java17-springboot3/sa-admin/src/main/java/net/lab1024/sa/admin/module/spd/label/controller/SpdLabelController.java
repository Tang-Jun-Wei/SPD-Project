package net.lab1024.sa.admin.module.spd.label.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.lab1024.sa.admin.module.spd.label.domain.form.SpdLabelForm;
import net.lab1024.sa.admin.module.spd.label.domain.form.SpdLabelQueryForm;
import net.lab1024.sa.admin.module.spd.label.domain.vo.SpdLabelVO;
import net.lab1024.sa.admin.module.spd.label.service.SpdLabelService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/spd/label")
@Tag(name = "SPD标签管理")
public class SpdLabelController {

    @Autowired
    private SpdLabelService spdLabelService;

    @Operation(summary = "分页查询")
    @PostMapping("/queryPage")
    @SaCheckPermission("spd:label:query")
    public ResponseDTO<PageResult<SpdLabelVO>> queryPage(@RequestBody @Valid SpdLabelQueryForm queryForm) {
        return ResponseDTO.ok(spdLabelService.queryPage(queryForm));
    }

    @Operation(summary = "查询详情")
    @GetMapping("/detail/{labelCode}")
    @SaCheckPermission("spd:label:query")
    public ResponseDTO<SpdLabelVO> getDetail(@PathVariable String labelCode) {
        return spdLabelService.getDetail(labelCode);
    }

    @Operation(summary = "新增标签")
    @PostMapping("/add")
    @SaCheckPermission("spd:label:add")
    public ResponseDTO<String> add(@RequestBody @Valid SpdLabelForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdLabelService.add(form, requestUser.getUserId().toString(), "default");
    }

    @Operation(summary = "更新标签")
    @PostMapping("/update")
    @SaCheckPermission("spd:label:update")
    public ResponseDTO<String> update(@RequestBody @Valid SpdLabelForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdLabelService.update(form, requestUser.getUserId().toString());
    }

    @Operation(summary = "删除标签")
    @GetMapping("/delete/{labelCode}")
    @SaCheckPermission("spd:label:delete")
    public ResponseDTO<String> delete(@PathVariable String labelCode) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdLabelService.delete(labelCode, requestUser.getUserId().toString());
    }

    @Operation(summary = "标签领用")
    @PostMapping("/use/{labelCode}")
    @SaCheckPermission("spd:label:use")
    public ResponseDTO<String> use(@PathVariable String labelCode) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdLabelService.use(labelCode, requestUser.getUserId().toString());
    }
}
