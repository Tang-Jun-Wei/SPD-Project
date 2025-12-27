package net.lab1024.sa.admin.module.spd.audit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import net.lab1024.sa.admin.module.spd.audit.domain.form.SpdDataAuditProcessForm;
import net.lab1024.sa.admin.module.spd.audit.domain.form.SpdDataAuditQueryForm;
import net.lab1024.sa.admin.module.spd.audit.domain.form.SpdDataAuditSubmitForm;
import net.lab1024.sa.admin.module.spd.audit.domain.vo.SpdDataAuditVO;
import net.lab1024.sa.admin.module.spd.audit.service.SpdDataAuditService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "SPD基础资料审核")
@RequestMapping("/spd/dataAudit")
public class SpdDataAuditController {

    @Resource
    private SpdDataAuditService spdDataAuditService;

    @Operation(summary = "分页查询审核记录")
    @PostMapping("/queryPage")
    public ResponseDTO<PageResult<SpdDataAuditVO>> queryPage(@RequestBody @Valid SpdDataAuditQueryForm queryForm) {
        return spdDataAuditService.queryPage(queryForm);
    }

    @Operation(summary = "查询审核详情")
    @GetMapping("/detail/{id}")
    public ResponseDTO<SpdDataAuditVO> getDetail(@PathVariable Long id) {
        return spdDataAuditService.getDetail(id);
    }

    @Operation(summary = "提交审核申请")
    @PostMapping("/submit")
    public ResponseDTO<String> submitAudit(@RequestBody @Valid SpdDataAuditSubmitForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdDataAuditService.submitAudit(form, requestUser.getUserId().toString(), "default");
    }

    @Operation(summary = "处理审核（通过/拒绝）")
    @PostMapping("/process")
    public ResponseDTO<String> processAudit(@RequestBody @Valid SpdDataAuditProcessForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdDataAuditService.processAudit(form, requestUser.getUserId().toString());
    }

    @Operation(summary = "撤销审核申请")
    @GetMapping("/cancel/{id}")
    public ResponseDTO<String> cancelAudit(@PathVariable Long id) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdDataAuditService.cancelAudit(id, requestUser.getUserId().toString());
    }
}
