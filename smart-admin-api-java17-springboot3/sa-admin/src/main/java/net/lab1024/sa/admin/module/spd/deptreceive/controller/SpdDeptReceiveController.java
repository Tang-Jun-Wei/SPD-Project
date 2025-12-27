package net.lab1024.sa.admin.module.spd.deptreceive.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import net.lab1024.sa.admin.module.spd.deptreceive.domain.vo.SpdDeptReceiveVO;
import net.lab1024.sa.admin.module.spd.deptreceive.service.SpdDeptReceiveService;
import net.lab1024.sa.base.common.domain.PageParam;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import org.springframework.web.bind.annotation.*;

/**
 * 科室收货管理Controller
 */
@RestController
@Tag(name = "科室收货管理")
@RequestMapping("/spd/dept-receive")
public class SpdDeptReceiveController {

    @Resource
    private SpdDeptReceiveService spdDeptReceiveService;

    /**
     * 分页查询科室收货单
     */
    @Operation(summary = "分页查询科室收货单")
    @PostMapping("/queryPage")
    public ResponseDTO<PageResult<SpdDeptReceiveVO>> queryPage(@RequestBody PageParam pageParam) {
        return spdDeptReceiveService.queryPage(pageParam);
    }

    /**
     * 查询科室收货单详情
     */
    @Operation(summary = "查询科室收货单详情")
    @GetMapping("/getDetail/{receiveId}")
    public ResponseDTO<SpdDeptReceiveVO> getDetail(@PathVariable String receiveId) {
        return spdDeptReceiveService.getDetail(receiveId);
    }

    /**
     * 确认收货
     */
    @Operation(summary = "确认收货")
    @PostMapping("/confirm")
    public ResponseDTO<String> confirmReceive(@RequestParam String receiveId) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdDeptReceiveService.confirmReceive(receiveId, requestUser.getUserId().toString());
    }

    /**
     * 拒收
     */
    @Operation(summary = "拒收")
    @PostMapping("/reject")
    public ResponseDTO<String> rejectReceive(@RequestParam String receiveId, 
                                              @RequestParam(required = false) String reason) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdDeptReceiveService.rejectReceive(receiveId, reason, requestUser.getUserId().toString());
    }
}
