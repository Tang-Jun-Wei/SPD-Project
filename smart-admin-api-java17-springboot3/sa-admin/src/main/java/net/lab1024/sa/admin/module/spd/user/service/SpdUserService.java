package net.lab1024.sa.admin.module.spd.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.spd.user.dao.SpdUserDao;
import net.lab1024.sa.admin.module.spd.user.domain.entity.SpdUserEntity;
import net.lab1024.sa.admin.module.spd.user.domain.form.SpdUserForm;
import net.lab1024.sa.admin.module.spd.user.domain.form.SpdUserQueryForm;
import net.lab1024.sa.admin.module.spd.user.domain.vo.SpdUserVO;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

/**
 * SPD用户管理 - 业务逻辑层
 * 
 * 功能说明：
 * 1. 处理用户管理的核心业务逻辑
 * 2. 实现用户的CRUD操作（创建、查询、更新、删除）
 * 3. 提供数据校验和业务规则验证
 * 4. 支持分页查询和条件筛选
 * 
 * 业务规则：
 * - 用户手机号必须唯一（排除已删除数据）
 * - 用户业务ID自动生成：USER+年月日+6位序号
 * - 默认租户ID为"default"
 * - 删除操作为逻辑删除（del_flag=1）
 * 
 * @since 2025-12-27
 */
@Service
@Slf4j
public class SpdUserService {

    @Resource
    private SpdUserDao spdUserDao;

    /**
     * 分页查询用户列表
     * 
     * 业务逻辑：
     * 1. 接收前端查询参数（用户名、手机号、角色等）
     * 2. 设置默认查询条件（del_flag=0，查询未删除数据）
     * 3. 调用Dao层执行分页查询
     * 4. 返回分页结果给前端
     * 
     * @param queryForm 查询表单（包含分页参数和查询条件）
     * @return 分页结果，包含用户VO列表
     */
    public ResponseDTO<PageResult<SpdUserVO>> queryByPage(SpdUserQueryForm queryForm) {
        // 设置默认查询未删除数据
        if (queryForm.getDelFlag() == null) {
            queryForm.setDelFlag(0);
        }
        
        // 构建分页参数
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        
        // 执行分页查询
        List<SpdUserVO> userList = spdUserDao.queryPage(page, queryForm);
        
        // 转换为分页结果
        PageResult<SpdUserVO> pageResult = SmartPageUtil.convert2PageResult(page, userList);
        
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 根据ID查询用户详情
     * 
     * 业务逻辑：
     * 1. 根据用户ID查询详细信息
     * 2. 校验用户是否存在且未删除
     * 3. 返回用户VO对象
     * 
     * @param userId 用户ID
     * @return 用户详情VO对象
     */
    public ResponseDTO<SpdUserVO> getDetail(Long userId) {
        // 查询用户详情（仅查询未删除数据）
        SpdUserVO userVO = spdUserDao.getDetail(userId, 0);
        
        // 校验用户是否存在
        if (Objects.isNull(userVO)) {
            return ResponseDTO.userErrorParam("用户不存在");
        }
        
        return ResponseDTO.ok(userVO);
    }

    /**
     * 新建用户
     * 
     * 业务逻辑：
     * 1. 校验手机号唯一性（排除已删除数据）
     * 2. 自动生成用户业务ID（USER+年月日+6位序号）
     * 3. 设置默认租户ID（default）
     * 4. 设置默认角色（1=普通用户）
     * 5. 插入数据库
     * 
     * @param createForm 新建表单（用户名、手机号、角色等）
     * @return 操作结果
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> createUser(SpdUserForm createForm) {
        // 1. 校验手机号唯一性
        SpdUserEntity existUser = spdUserDao.queryByPhone(createForm.getUserPhone(), null, 0);
        if (Objects.nonNull(existUser)) {
            return ResponseDTO.userErrorParam("手机号已存在");
        }
        
        // 2. 自动生成用户业务ID：USER+年月日+6位序号
        String userId = generateUserId();
        
        // 3. 复制表单数据到实体对象
        SpdUserEntity userEntity = SmartBeanUtil.copy(createForm, SpdUserEntity.class);
        userEntity.setUserId(userId);
        
        // 4. 设置默认值
        if (userEntity.getTenantId() == null || userEntity.getTenantId().isEmpty()) {
            userEntity.setTenantId("default");
        }
        if (userEntity.getUserRole() == null) {
            userEntity.setUserRole(1); // 默认普通用户
        }
        userEntity.setDelFlag(0); // 未删除
        
        // 5. 插入数据库
        spdUserDao.insert(userEntity);
        
        log.info("新建SPD用户成功，用户ID：{}，用户名：{}，手机号：{}", userId, createForm.getUserName(), createForm.getUserPhone());
        
        return ResponseDTO.ok();
    }

    /**
     * 编辑用户
     * 
     * 业务逻辑：
     * 1. 校验用户是否存在且未删除
     * 2. 校验手机号唯一性（排除当前编辑用户和已删除数据）
     * 3. 更新用户信息
     * 
     * @param updateForm 编辑表单（包含用户ID、用户名、手机号、角色等）
     * @return 操作结果
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> updateUser(SpdUserForm updateForm) {
        Long userId = updateForm.getId();
        
        // 1. 校验用户是否存在
        SpdUserEntity userEntity = spdUserDao.selectById(userId);
        if (Objects.isNull(userEntity) || userEntity.getDelFlag() == 1) {
            return ResponseDTO.userErrorParam("用户不存在");
        }
        
        // 2. 校验手机号唯一性（排除当前用户）
        SpdUserEntity existUser = spdUserDao.queryByPhone(updateForm.getUserPhone(), userId, 0);
        if (Objects.nonNull(existUser)) {
            return ResponseDTO.userErrorParam("手机号已存在");
        }
        
        // 3. 复制表单数据到实体对象（保留原有的user_id、tenant_id等字段）
        SpdUserEntity updateEntity = SmartBeanUtil.copy(updateForm, SpdUserEntity.class);
        updateEntity.setId(userId);
        
        // 4. 更新数据库
        spdUserDao.updateById(updateEntity);
        
        log.info("编辑SPD用户成功，用户ID：{}，用户名：{}，手机号：{}", userEntity.getUserId(), updateForm.getUserName(), updateForm.getUserPhone());
        
        return ResponseDTO.ok();
    }

    /**
     * 删除用户（逻辑删除）
     * 
     * 业务逻辑：
     * 1. 校验用户是否存在且未删除
     * 2. 设置del_flag=1（逻辑删除）
     * 3. 更新update_time和update_by
     * 
     * @param userId 用户ID
     * @param operatorId 操作人ID
     * @return 操作结果
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> deleteUser(Long userId, String operatorId) {
        // 1. 校验用户是否存在
        SpdUserEntity userEntity = spdUserDao.selectById(userId);
        if (Objects.isNull(userEntity) || userEntity.getDelFlag() == 1) {
            return ResponseDTO.userErrorParam("用户不存在");
        }
        
        // 2. 逻辑删除
        spdUserDao.deleteUser(userId, 1, operatorId);
        
        log.info("删除SPD用户成功，用户ID：{}，用户名：{}，操作人：{}", userEntity.getUserId(), userEntity.getUserName(), operatorId);
        
        return ResponseDTO.ok();
    }

    /**
     * 生成用户业务ID
     * 
     * 业务逻辑：
     * 1. 格式：USER+年月日+6位序号
     * 2. 示例：USER202512270001
     * 3. 查询当天最大序号，在此基础上+1
     * 4. 如果当天无数据，则从000001开始
     * 
     * @return 用户业务ID
     */
    private String generateUserId() {
        // 获取当前日期：yyyyMMdd
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String prefix = "USER" + dateStr;
        
        // 查询当天最大序号（简化实现，生产环境应使用分布式ID或数据库序列）
        // TODO: 生产环境建议使用Redis INCR或数据库序列保证唯一性
        long count = spdUserDao.selectCount(null);
        String sequence = String.format("%06d", (count % 1000000) + 1);
        
        return prefix + sequence;
    }
}
