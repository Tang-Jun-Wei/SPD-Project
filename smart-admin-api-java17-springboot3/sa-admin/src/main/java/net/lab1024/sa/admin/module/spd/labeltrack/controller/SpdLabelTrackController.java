package net.lab1024.sa.admin.module.spd.labeltrack.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.lab1024.sa.admin.module.spd.labeltrack.domain.form.SpdLabelTrackQueryForm;
import net.lab1024.sa.admin.module.spd.labeltrack.domain.vo.SpdLabelTrackVO;
import net.lab1024.sa.admin.module.spd.labeltrack.service.SpdLabelTrackService;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 标签追溯Controller
 *
 * @author 1024创新实验室
 */
@Tag(name = "标签追溯管理")
@RestController
@RequestMapping("/spd/label-track")
public class SpdLabelTrackController {

    @Autowired
    private SpdLabelTrackService spdLabelTrackService;

    /**
     * 查询标签追溯信息
     */
    @Operation(summary = "查询标签追溯信息")
    @PostMapping("/query")
    @SaCheckPermission("spd:labelTrack:query")
    public ResponseDTO<SpdLabelTrackVO> queryTrack(@RequestBody @Valid SpdLabelTrackQueryForm queryForm) {
        return spdLabelTrackService.queryTrack(queryForm);
    }
}
