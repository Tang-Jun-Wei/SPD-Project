package net.lab1024.sa.admin.module.spd.batch.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import net.lab1024.sa.admin.module.spd.batch.domain.form.SpdMaterialBatchForm;
import net.lab1024.sa.admin.module.spd.batch.domain.form.SpdMaterialBatchQueryForm;
import net.lab1024.sa.admin.module.spd.batch.domain.vo.SpdMaterialBatchVO;
import net.lab1024.sa.admin.module.spd.batch.service.SpdMaterialBatchService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "SPD批号管理")
public class SpdMaterialBatchController {

    @Autowired
    private SpdMaterialBatchService spdMaterialBatchService;

    @Operation(summary = "分页查询批号列表")
    @PostMapping("/spd/batch/query")
    @SaCheckPermission("spd:batch:query")
    public ResponseDTO<PageResult<SpdMaterialBatchVO>> queryPage(@RequestBody @Valid SpdMaterialBatchQueryForm queryForm) {
        return spdMaterialBatchService.queryPage(queryForm);
    }

    @Operation(summary = "查询批号详情")
    @GetMapping("/spd/batch/get/{id}")
    @SaCheckPermission("spd:batch:detail")
    public ResponseDTO<SpdMaterialBatchVO> getDetail(@PathVariable Long id) {
        return spdMaterialBatchService.getDetail(id);
    }

    @Operation(summary = "新增批号")
    @PostMapping("/spd/batch/add")
    @SaCheckPermission("spd:batch:add")
    public ResponseDTO<String> add(@RequestBody @Valid SpdMaterialBatchForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdMaterialBatchService.add(form, requestUser.getUserId().toString(), "default");
    }

    @Operation(summary = "编辑批号")
    @PostMapping("/spd/batch/update")
    @SaCheckPermission("spd:batch:update")
    public ResponseDTO<String> update(@RequestBody @Valid SpdMaterialBatchForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdMaterialBatchService.update(form, requestUser.getUserId().toString());
    }

    @Operation(summary = "删除批号")
    @GetMapping("/spd/batch/delete/{id}")
    @SaCheckPermission("spd:batch:delete")
    public ResponseDTO<String> delete(@PathVariable Long id) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdMaterialBatchService.delete(id, requestUser.getUserId().toString());
    }
}
