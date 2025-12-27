package net.lab1024.sa.admin.module.spd.orgnode.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.lab1024.sa.admin.constant.AdminSwaggerTagConst;
import net.lab1024.sa.admin.module.spd.orgnode.domain.form.SpdOrgNodeForm;
import net.lab1024.sa.admin.module.spd.orgnode.domain.form.SpdOrgNodeQueryForm;
import net.lab1024.sa.admin.module.spd.orgnode.domain.vo.SpdOrgNodeVO;
import net.lab1024.sa.admin.module.spd.orgnode.service.SpdOrgNodeService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * SPD组织节点管理 - 控制器层
 * 
 * 功能说明：
 * 1. 提供组织节点的增删改查接口
 * 2. 支持分页查询和条件查询
 * 3. 提供科室和仓库的下拉选择接口
 * 4. 支持状态管理（启用/禁用）
 * 5. 集成权限控制和参数校验
 * 
 * 接口列表：
 * - POST /spd/orgNode/query 分页查询
 * - GET /spd/orgNode/get/{nodeId} 查询详情
 * - POST /spd/orgNode/add 新增节点
 * - POST /spd/orgNode/update 编辑节点
 * - GET /spd/orgNode/delete/{nodeId} 删除节点
 * - GET /spd/orgNode/deptList 科室下拉列表
 * - GET /spd/orgNode/warehouseList 仓库下拉列表
 * 
 * @since 2025-12-27
 */
@RestController
@Tag(name = "SPD组织节点管理")
public class SpdOrgNodeController {

    @Autowired
    private SpdOrgNodeService spdOrgNodeService;

    /**
     * 分页查询组织节点列表
     * 
     * 业务逻辑：
     * 1. 接收查询条件（节点名称、类型、上级节点等）
     * 2. 调用Service层执行分页查询
     * 3. 返回包含类型名称、状态名称的VO对象
     * 
     * @param queryForm 查询条件
     * @return 分页结果
     */
    @Operation(summary = "分页查询组织节点列表")
    @PostMapping("/spd/orgNode/query")
    @SaCheckPermission("spd:orgNode:query")
    public ResponseDTO<PageResult<SpdOrgNodeVO>> queryPage(@RequestBody @Valid SpdOrgNodeQueryForm queryForm) {
        return spdOrgNodeService.queryPage(queryForm);
    }

    /**
     * 查询组织节点详情
     * 
     * 业务逻辑：
     * 1. 根据节点ID查询详细信息
     * 2. 包含类型名称、状态名称、上级节点名称
     * 3. 校验节点是否存在
     * 
     * @param nodeId 节点ID
     * @return 节点详情
     */
    @Operation(summary = "查询组织节点详情")
    @GetMapping("/spd/orgNode/get/{nodeId}")
    @SaCheckPermission("spd:orgNode:detail")
    public ResponseDTO<SpdOrgNodeVO> getDetail(@PathVariable Long nodeId) {
        return spdOrgNodeService.getDetail(nodeId);
    }

    /**
     * 新增组织节点
     * 
     * 业务逻辑：
     * 1. 校验节点名称+类型唯一性
     * 2. 自动生成节点业务ID（DEPT/WH+年月日+序号）
     * 3. 设置默认状态（启用）
     * 4. 保存到数据库
     * 
     * @param form 新增表单
     * @return 操作结果
     */
    @Operation(summary = "新增组织节点")
    @PostMapping("/spd/orgNode/add")
    @SaCheckPermission("spd:orgNode:add")
    public ResponseDTO<String> add(@RequestBody @Valid SpdOrgNodeForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        // TODO: 租户ID从实际业务获取，暂时使用默认值
        String tenantId = "default";
        return spdOrgNodeService.add(form, requestUser.getUserId().toString(), tenantId);
    }

    /**
     * 编辑组织节点
     * 
     * 业务逻辑：
     * 1. 校验节点是否存在
     * 2. 校验节点名称+类型唯一性（排除自身）
     * 3. 更新节点信息
     * 
     * @param form 编辑表单
     * @return 操作结果
     */
    @Operation(summary = "编辑组织节点")
    @PostMapping("/spd/orgNode/update")
    @SaCheckPermission("spd:orgNode:update")
    public ResponseDTO<String> update(@RequestBody @Valid SpdOrgNodeForm form) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdOrgNodeService.update(form, requestUser.getUserId().toString());
    }

    /**
     * 删除组织节点
     * 
     * 业务逻辑：
     * 1. 校验节点是否存在
     * 2. 校验节点是否被引用
     * 3. 执行逻辑删除
     * 
     * @param nodeId 节点ID
     * @return 操作结果
     */
    @Operation(summary = "删除组织节点")
    @GetMapping("/spd/orgNode/delete/{nodeId}")
    @SaCheckPermission("spd:orgNode:delete")
    public ResponseDTO<String> delete(@PathVariable Long nodeId) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdOrgNodeService.delete(nodeId, requestUser.getUserId().toString());
    }

    /**
     * 查询科室下拉列表
     * 
     * 业务逻辑：
     * 1. 仅查询启用状态的科室节点
     * 2. 按租户筛选
     * 3. 返回简化的VO对象（用于前端下拉选择）
     * 
     * @return 科室列表
     */
    @Operation(summary = "查询科室下拉列表")
    @GetMapping("/spd/orgNode/deptList")
    @SaCheckPermission("spd:orgNode:query")
    public ResponseDTO<List<SpdOrgNodeVO>> queryDeptList() {
        // TODO: 租户ID从实际业务获取，暂时使用默认值
        String tenantId = "default";
        return spdOrgNodeService.queryDeptList(tenantId);
    }

    /**
     * 查询仓库下拉列表
     * 
     * 业务逻辑：
     * 1. 仅查询启用状态的仓库节点
     * 2. 按租户筛选
     * 3. 返回简化的VO对象（用于前端下拉选择）
     * 
     * @return 仓库列表
     */
    @Operation(summary = "查询仓库下拉列表")
    @GetMapping("/spd/orgNode/warehouseList")
    @SaCheckPermission("spd:orgNode:query")
    public ResponseDTO<List<SpdOrgNodeVO>> queryWarehouseList() {
        // TODO: 租户ID从实际业务获取，暂时使用默认值
        String tenantId = "default";
        return spdOrgNodeService.queryWarehouseList(tenantId);
    }

    /**
     * 更新仓库状态
     * 
     * 业务逻辑：
     * 1. 校验节点是否为仓库类型
     * 2. 更新仓库状态（启用/禁用）
     * 
     * @param nodeId 节点ID
     * @param warehouseStatus 仓库状态（1=启用 0=禁用）
     * @return 操作结果
     */
    @Operation(summary = "更新仓库状态")
    @GetMapping("/spd/orgNode/updateWarehouseStatus/{nodeId}/{warehouseStatus}")
    @SaCheckPermission("spd:orgNode:update")
    public ResponseDTO<String> updateWarehouseStatus(@PathVariable Long nodeId, @PathVariable Integer warehouseStatus) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdOrgNodeService.updateWarehouseStatus(nodeId, warehouseStatus, requestUser.getUserId().toString());
    }

    /**
     * 更新科室状态
     * 
     * 业务逻辑：
     * 1. 校验节点是否为科室类型
     * 2. 更新科室状态（启用/禁用）
     * 
     * @param nodeId 节点ID
     * @param deptStatus 科室状态（1=启用 0=禁用）
     * @return 操作结果
     */
    @Operation(summary = "更新科室状态")
    @GetMapping("/spd/orgNode/updateDeptStatus/{nodeId}/{deptStatus}")
    @SaCheckPermission("spd:orgNode:update")
    public ResponseDTO<String> updateDeptStatus(@PathVariable Long nodeId, @PathVariable Integer deptStatus) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return spdOrgNodeService.updateDeptStatus(nodeId, deptStatus, requestUser.getUserId().toString());
    }
}
