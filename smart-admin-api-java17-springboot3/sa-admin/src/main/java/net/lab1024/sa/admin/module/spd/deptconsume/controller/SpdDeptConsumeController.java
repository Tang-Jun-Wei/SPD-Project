package net.lab1024.sa.admin.module.spd.deptconsume.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.lab1024.sa.admin.module.spd.deptconsume.domain.form.SpdDeptConsumeAddForm;
import net.lab1024.sa.admin.module.spd.deptconsume.domain.form.SpdDeptConsumeQueryForm;
import net.lab1024.sa.admin.module.spd.deptconsume.domain.vo.SpdDeptConsumeVO;
import net.lab1024.sa.admin.module.spd.deptconsume.service.SpdDeptConsumeService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 科室消耗Controller
 *
 * @author 1024创新实验室
 */
@Tag(name = "科室消耗管理")
@RestController
@RequestMapping("/spd/dept-consume")
public class SpdDeptConsumeController {

    @Autowired
    private SpdDeptConsumeService spdDeptConsumeService;

    /**
     * 分页查询
     */
    @Operation(summary = "分页查询")
    @PostMapping("/queryPage")
    @SaCheckPermission("spd:deptConsume:query")
    public ResponseDTO<PageResult<SpdDeptConsumeVO>> queryPage(@RequestBody @Valid SpdDeptConsumeQueryForm queryForm) {
        return spdDeptConsumeService.queryPage(queryForm);
    }

    /**
     * 查询详情
     */
    @Operation(summary = "查询详情")
    @GetMapping("/getDetail/{consumeId}")
    @SaCheckPermission("spd:deptConsume:query")
    public ResponseDTO<SpdDeptConsumeVO> getDetail(@PathVariable Long consumeId) {
        return spdDeptConsumeService.getDetail(consumeId);
    }

    /**
     * 新增消耗
     */
    @Operation(summary = "新增消耗")
    @PostMapping("/add")
    @SaCheckPermission("spd:deptConsume:add")
    public ResponseDTO<String> add(@RequestBody @Valid SpdDeptConsumeAddForm addForm) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdDeptConsumeService.add(addForm, requestUser.getUserId(), requestUser.getUserName());
    }

    /**
     * 反消耗
     */
    @Operation(summary = "反消耗")
    @PostMapping("/reverse")
    @SaCheckPermission("spd:deptConsume:reverse")
    public ResponseDTO<String> reverse(@RequestParam Long consumeId, @RequestParam(required = false) String reason) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdDeptConsumeService.reverse(consumeId, reason, requestUser.getUserId(), requestUser.getUserName());
    }

    /**
     * 删除
     */
    @Operation(summary = "删除")
    @GetMapping("/delete/{consumeId}")
    @SaCheckPermission("spd:deptConsume:delete")
    public ResponseDTO<String> delete(@PathVariable Long consumeId) {
        return spdDeptConsumeService.delete(consumeId);
    }
}
