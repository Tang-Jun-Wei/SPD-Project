package net.lab1024.sa.admin.module.spd.stocklimit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import net.lab1024.sa.admin.module.spd.stocklimit.domain.form.SpdStockLimitForm;
import net.lab1024.sa.admin.module.spd.stocklimit.domain.form.SpdStockLimitQueryForm;
import net.lab1024.sa.admin.module.spd.stocklimit.domain.vo.SpdStockLimitVO;
import net.lab1024.sa.admin.module.spd.stocklimit.service.SpdStockLimitService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "SPD库存安全量管理")
@RequestMapping("/spd/stockLimit")
public class SpdStockLimitController {

    @Resource
    private SpdStockLimitService spdStockLimitService;

    @Operation(summary = "分页查询库存安全量配置")
    @PostMapping("/queryPage")
    public ResponseDTO<PageResult<SpdStockLimitVO>> queryPage(@RequestBody @Valid SpdStockLimitQueryForm queryForm) {
        return spdStockLimitService.queryPage(queryForm);
    }

    @Operation(summary = "查询库存安全量详情")
    @GetMapping("/detail/{id}")
    public ResponseDTO<SpdStockLimitVO> getDetail(@PathVariable Long id) {
        return spdStockLimitService.getDetail(id);
    }

    @Operation(summary = "新增库存安全量配置")
    @PostMapping("/add")
    public ResponseDTO<String> add(@RequestBody @Valid SpdStockLimitForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdStockLimitService.add(form, requestUser.getUserId().toString(), "default");
    }

    @Operation(summary = "更新库存安全量配置")
    @PostMapping("/update")
    public ResponseDTO<String> update(@RequestBody @Valid SpdStockLimitForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdStockLimitService.update(form, requestUser.getUserId().toString());
    }

    @Operation(summary = "删除库存安全量配置")
    @GetMapping("/delete/{id}")
    public ResponseDTO<String> delete(@PathVariable Long id) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdStockLimitService.delete(id, requestUser.getUserId().toString());
    }
}
