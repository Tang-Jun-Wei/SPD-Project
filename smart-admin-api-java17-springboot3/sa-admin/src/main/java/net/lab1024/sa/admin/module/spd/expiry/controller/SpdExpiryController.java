package net.lab1024.sa.admin.module.spd.expiry.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.lab1024.sa.admin.module.spd.expiry.domain.form.SpdExpiryWarningQueryForm;
import net.lab1024.sa.admin.module.spd.expiry.domain.vo.SpdExpiryStatisticsVO;
import net.lab1024.sa.admin.module.spd.expiry.domain.vo.SpdExpiryWarningVO;
import net.lab1024.sa.admin.module.spd.expiry.service.SpdExpiryService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * 库存效期Controller
 *
 * @author 1024创新实验室
 */
@Tag(name = "库存效期管理")
@RestController
@RequestMapping("/spd/expiry")
public class SpdExpiryController {

    @Autowired
    private SpdExpiryService spdExpiryService;

    /**
     * 查询效期统计
     */
    @Operation(summary = "查询效期统计")
    @GetMapping("/statistics")
    @SaCheckPermission("spd:expiry:query")
    public ResponseDTO<SpdExpiryStatisticsVO> getStatistics() {
        return spdExpiryService.getStatistics();
    }

    /**
     * 分页查询效期预警列表
     */
    @Operation(summary = "分页查询效期预警列表")
    @PostMapping("/queryWarningPage")
    @SaCheckPermission("spd:expiry:query")
    public ResponseDTO<PageResult<SpdExpiryWarningVO>> queryWarningPage(@RequestBody @Valid SpdExpiryWarningQueryForm queryForm) {
        return spdExpiryService.queryWarningPage(queryForm);
    }
}
