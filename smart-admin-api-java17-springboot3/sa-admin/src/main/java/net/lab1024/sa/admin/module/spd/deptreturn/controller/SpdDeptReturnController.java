package net.lab1024.sa.admin.module.spd.deptreturn.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import net.lab1024.sa.admin.module.spd.deptreturn.domain.form.SpdDeptReturnForm;
import net.lab1024.sa.admin.module.spd.deptreturn.domain.vo.SpdDeptReturnVO;
import net.lab1024.sa.admin.module.spd.deptreturn.service.SpdDeptReturnService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "科室退库管理")
@RequestMapping("/spd/return")
public class SpdDeptReturnController {

    @Resource
    private SpdDeptReturnService spdDeptReturnService;

    /**
     * 分页查询退库单
     */
    @Operation(summary = "分页查询退库单")
    @PostMapping("/queryPage")
    public ResponseDTO<PageResult<SpdDeptReturnVO>> queryPage(@RequestBody Object form) {
        return spdDeptReturnService.queryPage(form);
    }

    /**
     * 查询退库单详情
     */
    @Operation(summary = "查询退库单详情")
    @GetMapping("/getDetail/{returnId}")
    public ResponseDTO<SpdDeptReturnVO> getDetail(@PathVariable String returnId) {
        return spdDeptReturnService.getDetail(returnId);
    }

    /**
     * 新增退库单
     */
    @Operation(summary = "新增退库单")
    @PostMapping("/add")
    public ResponseDTO<String> add(@RequestBody SpdDeptReturnForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdDeptReturnService.add(form, requestUser.getUserId().toString(), "default");
    }

    /**
     * 审核退库单
     */
    @Operation(summary = "审核退库单")
    @PostMapping("/audit")
    public ResponseDTO<String> audit(@RequestParam String returnId, 
                                     @RequestParam Integer auditStatus,
                                     @RequestParam(required = false) String auditOpinion) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdDeptReturnService.audit(returnId, auditStatus, auditOpinion, requestUser.getUserId().toString());
    }

    /**
     * 删除退库单
     */
    @Operation(summary = "删除退库单")
    @GetMapping("/delete/{returnId}")
    public ResponseDTO<String> delete(@PathVariable String returnId) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdDeptReturnService.delete(returnId, requestUser.getUserId().toString());
    }
}
