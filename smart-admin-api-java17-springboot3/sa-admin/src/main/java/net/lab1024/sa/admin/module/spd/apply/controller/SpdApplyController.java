package net.lab1024.sa.admin.module.spd.apply.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.lab1024.sa.admin.module.spd.apply.domain.form.SpdApplyForm;
import net.lab1024.sa.admin.module.spd.apply.domain.form.SpdApplyQueryForm;
import net.lab1024.sa.admin.module.spd.apply.domain.vo.SpdApplyVO;
import net.lab1024.sa.admin.module.spd.apply.service.SpdApplyService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@Tag(name = "SPD申领管理")
@RestController
@RequestMapping("/spd/apply")
public class SpdApplyController {

    @Autowired
    private SpdApplyService spdApplyService;

    @Operation(summary = "分页查询申领单")
    @PostMapping("/queryPage")
    @SaCheckPermission("spd:apply:query")
    public ResponseDTO<PageResult<SpdApplyVO>> queryPage(@RequestBody @Valid SpdApplyQueryForm queryForm) {
        return ResponseDTO.ok(spdApplyService.queryPage(queryForm));
    }

    @Operation(summary = "查询申领单详情")
    @GetMapping("/detail/{id}")
    @SaCheckPermission("spd:apply:detail")
    public ResponseDTO<SpdApplyVO> getDetail(@PathVariable Long id) {
        return spdApplyService.getDetail(id);
    }

    @Operation(summary = "新增申领单")
    @PostMapping("/add")
    @SaCheckPermission("spd:apply:add")
    public ResponseDTO<String> add(@RequestBody @Valid SpdApplyForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        String tenantId = "default";
        return spdApplyService.add(form, requestUser.getUserId().toString(), tenantId);
    }

    @Operation(summary = "更新申领单")
    @PostMapping("/update")
    @SaCheckPermission("spd:apply:update")
    public ResponseDTO<String> update(@RequestBody @Valid SpdApplyForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdApplyService.update(form, requestUser.getUserId().toString());
    }

    @Operation(summary = "审核申领单")
    @PostMapping("/approve/{id}")
    @SaCheckPermission("spd:apply:approve")
    public ResponseDTO<String> approve(
            @PathVariable Long id,
            @RequestParam Integer approveStatus,
            @RequestParam(required = false) String approveRemark) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdApplyService.approve(id, approveStatus, approveRemark,
                requestUser.getUserId().toString(), requestUser.getUserName());
    }

    @Operation(summary = "删除申领单")
    @GetMapping("/delete/{id}")
    @SaCheckPermission("spd:apply:delete")
    public ResponseDTO<String> delete(@PathVariable Long id) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdApplyService.delete(id, requestUser.getUserId().toString());
    }
}
