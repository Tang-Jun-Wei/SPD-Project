package net.lab1024.sa.admin.module.spd.user.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import net.lab1024.sa.admin.module.spd.user.domain.form.SpdUserForm;
import net.lab1024.sa.admin.module.spd.user.domain.form.SpdUserQueryForm;
import net.lab1024.sa.admin.module.spd.user.domain.vo.SpdUserVO;
import net.lab1024.sa.admin.module.spd.user.service.SpdUserService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import org.springframework.web.bind.annotation.*;

/**
 * SPD用户管理 - 控制器层
 * 
 * 功能说明：
 * 1. 提供RESTful API接口
 * 2. 处理前端HTTP请求并调用Service层
 * 3. 参数校验和权限控制
 * 4. 使用Swagger注解生成API文档
 * 
 * API规范：
 * - 所有接口路径以/spd/user开头
 * - 使用POST接收复杂参数，GET接收简单参数
 * - 统一返回ResponseDTO包装结果
 * - 使用Sa-Token进行权限控制
 * 
 * @since 2025-12-27
 */
@RestController
@Tag(name = "SPD用户管理")
public class SpdUserController {

    @Resource
    private SpdUserService spdUserService;

    /**
     * 分页查询用户列表
     * 
     * 接口说明：
     * 1. 接收前端分页查询请求
     * 2. 支持按用户名、手机号、角色等条件筛选
     * 3. 返回分页结果
     * 
     * 权限：spd:user:query
     * 
     * @param queryForm 查询表单（分页参数+查询条件）
     * @return 分页结果
     */
    @Operation(summary = "分页查询用户列表")
    @PostMapping("/spd/user/page/query")
    @SaCheckPermission("spd:user:query")
    public ResponseDTO<PageResult<SpdUserVO>> queryByPage(@RequestBody @Valid SpdUserQueryForm queryForm) {
        return spdUserService.queryByPage(queryForm);
    }

    /**
     * 查询用户详情
     * 
     * 接口说明：
     * 1. 根据用户ID查询详细信息
     * 2. 用于编辑前回显数据
     * 
     * 权限：spd:user:query
     * 
     * @param userId 用户ID
     * @return 用户详情VO
     */
    @Operation(summary = "查询用户详情")
    @GetMapping("/spd/user/get/{userId}")
    @SaCheckPermission("spd:user:query")
    public ResponseDTO<SpdUserVO> getDetail(@PathVariable Long userId) {
        return spdUserService.getDetail(userId);
    }

    /**
     * 新建用户
     * 
     * 接口说明：
     * 1. 接收前端提交的用户新建表单
     * 2. 自动生成用户业务ID
     * 3. 校验手机号唯一性
     * 4. 插入数据库
     * 
     * 权限：spd:user:add
     * 
     * @param createForm 新建表单
     * @return 操作结果
     */
    @Operation(summary = "新建用户")
    @PostMapping("/spd/user/create")
    @SaCheckPermission("spd:user:add")
    public ResponseDTO<String> createUser(@RequestBody @Valid SpdUserForm createForm) {
        // 获取当前登录用户信息（用于记录创建人）
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        // TODO: 设置创建人信息（根据实际业务需求）
        
        return spdUserService.createUser(createForm);
    }

    /**
     * 编辑用户
     * 
     * 接口说明：
     * 1. 接收前端提交的用户编辑表单
     * 2. 校验用户是否存在
     * 3. 校验手机号唯一性（排除当前用户）
     * 4. 更新数据库
     * 
     * 权限：spd:user:update
     * 
     * @param updateForm 编辑表单（必须包含用户ID）
     * @return 操作结果
     */
    @Operation(summary = "编辑用户")
    @PostMapping("/spd/user/update")
    @SaCheckPermission("spd:user:update")
    public ResponseDTO<String> updateUser(@RequestBody @Valid SpdUserForm updateForm) {
        return spdUserService.updateUser(updateForm);
    }

    /**
     * 删除用户（逻辑删除）
     * 
     * 接口说明：
     * 1. 根据用户ID逻辑删除用户
     * 2. 设置del_flag=1
     * 3. 记录删除操作人
     * 
     * 权限：spd:user:delete
     * 
     * @param userId 用户ID
     * @return 操作结果
     */
    @Operation(summary = "删除用户")
    @GetMapping("/spd/user/delete/{userId}")
    @SaCheckPermission("spd:user:delete")
    public ResponseDTO<String> deleteUser(@PathVariable Long userId) {
        // 获取当前登录用户ID（用于记录操作人）
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        String operatorId = String.valueOf(requestUser.getUserId());
        
        return spdUserService.deleteUser(userId, operatorId);
    }
}