package net.lab1024.sa.admin.module.spd.warehouseuser.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import net.lab1024.sa.admin.module.spd.warehouseuser.domain.form.SpdWarehouseUserForm;
import net.lab1024.sa.admin.module.spd.warehouseuser.domain.form.SpdWarehouseUserQueryForm;
import net.lab1024.sa.admin.module.spd.warehouseuser.domain.vo.SpdWarehouseUserVO;
import net.lab1024.sa.admin.module.spd.warehouseuser.service.SpdWarehouseUserService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "SPD仓库人员对应关系管理")
@RequestMapping("/spd/warehouseUser")
public class SpdWarehouseUserController {

    @Resource
    private SpdWarehouseUserService spdWarehouseUserService;

    @Operation(summary = "分页查询仓库人员关系")
    @PostMapping("/queryPage")
    public ResponseDTO<PageResult<SpdWarehouseUserVO>> queryPage(@RequestBody @Valid SpdWarehouseUserQueryForm queryForm) {
        return spdWarehouseUserService.queryPage(queryForm);
    }

    @Operation(summary = "查询仓库人员关系详情")
    @GetMapping("/detail/{id}")
    public ResponseDTO<SpdWarehouseUserVO> getDetail(@PathVariable Long id) {
        return spdWarehouseUserService.getDetail(id);
    }

    @Operation(summary = "新增仓库人员关系")
    @PostMapping("/add")
    public ResponseDTO<String> add(@RequestBody @Valid SpdWarehouseUserForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdWarehouseUserService.add(form, requestUser.getUserId().toString(), "default");
    }

    @Operation(summary = "更新仓库人员关系")
    @PostMapping("/update")
    public ResponseDTO<String> update(@RequestBody @Valid SpdWarehouseUserForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdWarehouseUserService.update(form, requestUser.getUserId().toString());
    }

    @Operation(summary = "删除仓库人员关系")
    @GetMapping("/delete/{id}")
    public ResponseDTO<String> delete(@PathVariable Long id) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdWarehouseUserService.delete(id, requestUser.getUserId().toString());
    }

    @Operation(summary = "更新状态")
    @GetMapping("/updateStatus/{id}/{status}")
    public ResponseDTO<String> updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdWarehouseUserService.updateStatus(id, status, requestUser.getUserId().toString());
    }

    @Operation(summary = "根据仓库ID查询人员列表")
    @GetMapping("/queryByWarehouse/{warehouseId}")
    public ResponseDTO<List<SpdWarehouseUserVO>> queryByWarehouse(@PathVariable String warehouseId) {
        return spdWarehouseUserService.queryByWarehouse(warehouseId);
    }

    @Operation(summary = "根据用户ID查询仓库列表")
    @GetMapping("/queryByUser/{userId}")
    public ResponseDTO<List<SpdWarehouseUserVO>> queryByUser(@PathVariable String userId) {
        return spdWarehouseUserService.queryByUser(userId);
    }
}
