package net.lab1024.sa.admin.module.spd.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.spd.user.domain.entity.SpdUserEntity;
import net.lab1024.sa.admin.module.spd.user.domain.form.SpdUserQueryForm;
import net.lab1024.sa.admin.module.spd.user.domain.vo.SpdUserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * SPD用户管理 - 数据访问层
 * 
 * 功能说明：
 * 1. 继承MyBatis-Plus的BaseMapper，提供基础CRUD操作
 * 2. 定义自定义查询方法
 * 3. 支持分页查询、条件查询、详情查询
 * 
 * 业务规则：
 * - 所有查询默认过滤已删除数据（del_flag=1）
 * - 支持按手机号、角色、租户等条件查询
 * - 手机号唯一性校验（排除已删除和当前编辑记录）
 * 
 * @since 2025-12-27
 */
@Mapper
public interface SpdUserDao extends BaseMapper<SpdUserEntity> {

    /**
     * 分页查询用户列表
     * 
     * 业务逻辑：
     * 1. 支持按用户姓名模糊查询
     * 2. 支持按手机号精确查询
     * 3. 支持按角色筛选
     * 4. 支持按租户筛选
     * 5. 默认过滤已删除数据
     * 
     * @param page 分页参数
     * @param queryForm 查询条件
     * @return 用户VO列表
     */
    List<SpdUserVO> queryPage(Page page, @Param("queryForm") SpdUserQueryForm queryForm);

    /**
     * 根据ID查询用户详情
     * 
     * 业务逻辑：
     * 1. 返回用户完整信息
     * 2. 包含角色名称转换
     * 3. 校验删除状态
     * 
     * @param userId 用户ID
     * @param delFlag 删除标识（0=正常 1=已删除）
     * @return 用户VO对象
     */
    SpdUserVO getDetail(@Param("userId") Long userId, @Param("delFlag") Integer delFlag);

    /**
     * 根据手机号查询用户（用于唯一性校验）
     * 
     * 业务逻辑：
     * 1. 查询指定手机号的用户
     * 2. 可排除指定ID（编辑时使用）
     * 3. 可指定删除状态
     * 
     * @param userPhone 手机号
     * @param excludeUserId 排除的用户ID（编辑时传入）
     * @param delFlag 删除标识
     * @return 用户实体对象
     */
    SpdUserEntity queryByPhone(@Param("userPhone") String userPhone, 
                               @Param("excludeUserId") Long excludeUserId, 
                               @Param("delFlag") Integer delFlag);

    /**
     * 逻辑删除用户
     * 
     * 业务逻辑：
     * 1. 设置del_flag=1
     * 2. 更新update_time和update_by
     * 
     * @param userId 用户ID
     * @param delFlag 删除标识（1表示删除）
     * @param updateBy 操作人ID
     */
    void deleteUser(@Param("userId") Long userId, 
                   @Param("delFlag") Integer delFlag, 
                   @Param("updateBy") String updateBy);
}
