package net.lab1024.sa.admin.module.spd.batch.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.lab1024.sa.admin.module.spd.batch.domain.form.SpdBatchForm;
import net.lab1024.sa.admin.module.spd.batch.domain.form.SpdBatchQueryForm;
import net.lab1024.sa.admin.module.spd.batch.domain.vo.SpdBatchVO;
import net.lab1024.sa.admin.module.spd.batch.service.SpdBatchService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * 批号信息Controller
 */
@Tag(name = "SPD批号管理")
@RestController
@RequestMapping("/spd/batch")
public class SpdBatchController {

    @Autowired
    private SpdBatchService spdBatchService;

    @Operation(summary = "分页查询批号")
    @PostMapping("/queryPage")
    @SaCheckPermission("spd:batch:query")
    public ResponseDTO<PageResult<SpdBatchVO>> queryPage(@RequestBody @Valid SpdBatchQueryForm queryForm) {
        return ResponseDTO.ok(spdBatchService.queryPage(queryForm));
    }

    @Operation(summary = "新增批号")
    @PostMapping("/add")
    @SaCheckPermission("spd:batch:add")
    public ResponseDTO<String> add(@RequestBody @Valid SpdBatchForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        String tenantId = "default";
        return spdBatchService.add(form, requestUser.getUserId().toString(), tenantId);
    }

    @Operation(summary = "修改批号")
    @PostMapping("/update")
    @SaCheckPermission("spd:batch:update")
    public ResponseDTO<String> update(@RequestBody @Valid SpdBatchForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdBatchService.update(form, requestUser.getUserId().toString());
    }

    @Operation(summary = "删除批号")
    @GetMapping("/delete/{id}")
    @SaCheckPermission("spd:batch:delete")
    public ResponseDTO<String> delete(@PathVariable Long id) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdBatchService.delete(id, requestUser.getUserId().toString());
    }
}
