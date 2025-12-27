package net.lab1024.sa.admin.module.spd.orgnode.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.spd.orgnode.domain.entity.SpdOrgNodeEntity;
import net.lab1024.sa.admin.module.spd.orgnode.domain.form.SpdOrgNodeQueryForm;
import net.lab1024.sa.admin.module.spd.orgnode.domain.vo.SpdOrgNodeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * SPD组织节点管理 - 数据访问层
 * 
 * 功能说明：
 * 1. 继承MyBatis-Plus的BaseMapper，提供基础CRUD操作
 * 2. 定义自定义查询方法
 * 3. 支持分页查询、树形查询、状态管理
 * 
 * 业务规则：
 * - 所有查询默认过滤已删除数据（del_flag=0）
 * - 支持按节点类型、上级节点、租户等条件查询
 * - 节点名称+类型唯一性校验（排除已删除）
 * 
 * @since 2025-12-27
 */
@Mapper
public interface SpdOrgNodeDao extends BaseMapper<SpdOrgNodeEntity> {

    /**
     * 分页查询组织节点列表
     * 
     * 业务逻辑：
     * 1. 支持按节点名称模糊查询
     * 2. 支持按节点类型筛选（科室/仓库）
     * 3. 支持按上级节点筛选
     * 4. 支持按租户筛选
     * 5. 默认过滤已删除数据
     * 6. 返回包含关联信息的VO对象
     * 
     * @param page 分页参数
     * @param queryForm 查询条件
     * @return 组织节点VO列表
     */
    List<SpdOrgNodeVO> queryPage(Page page, @Param("queryForm") SpdOrgNodeQueryForm queryForm);

    /**
     * 根据ID查询组织节点详情
     * 
     * 业务逻辑：
     * 1. 返回节点完整信息
     * 2. 包含类型名称、状态名称转换
     * 3. 包含上级节点名称
     * 4. 校验删除状态
     * 
     * @param nodeId 节点ID
     * @param delFlag 删除标识（0=正常 1=已删除）
     * @return 组织节点VO对象
     */
    SpdOrgNodeVO getDetail(@Param("nodeId") Long nodeId, @Param("delFlag") Integer delFlag);

    /**
     * 根据节点名称和类型查询节点（用于唯一性校验）
     * 
     * 业务逻辑：
     * 1. 查询指定名称和类型的节点
     * 2. 可排除指定ID（编辑时使用）
     * 3. 可指定删除状态
     * 
     * @param nodeName 节点名称
     * @param nodeType 节点类型
     * @param excludeNodeId 排除的节点ID（编辑时传入）
     * @param delFlag 删除标识
     * @return 节点实体对象
     */
    SpdOrgNodeEntity queryByNameAndType(@Param("nodeName") String nodeName,
                                        @Param("nodeType") Integer nodeType,
                                        @Param("excludeNodeId") Long excludeNodeId,
                                        @Param("delFlag") Integer delFlag);

    /**
     * 查询所有科室节点（用于下拉选择）
     * 
     * 业务逻辑：
     * 1. 仅查询节点类型为科室（node_type=1）
     * 2. 仅查询启用状态（dept_status=1）
     * 3. 过滤已删除数据
     * 4. 按租户筛选
     * 
     * @param tenantId 租户ID
     * @param delFlag 删除标识
     * @return 科室节点列表
     */
    List<SpdOrgNodeVO> queryDeptList(@Param("tenantId") String tenantId, @Param("delFlag") Integer delFlag);

    /**
     * 查询所有仓库节点（用于下拉选择）
     * 
     * 业务逻辑：
     * 1. 仅查询节点类型为仓库（node_type=2）
     * 2. 仅查询启用状态（warehouse_status=1）
     * 3. 过滤已删除数据
     * 4. 按租户筛选
     * 
     * @param tenantId 租户ID
     * @param delFlag 删除标识
     * @return 仓库节点列表
     */
    List<SpdOrgNodeVO> queryWarehouseList(@Param("tenantId") String tenantId, @Param("delFlag") Integer delFlag);

    /**
     * 逻辑删除组织节点
     * 
     * 业务逻辑：
     * 1. 设置del_flag=1
     * 2. 更新update_time和update_by
     * 
     * @param nodeId 节点ID
     * @param delFlag 删除标识（1表示删除）
     * @param updateBy 操作人ID
     */
    void deleteNode(@Param("nodeId") Long nodeId,
                    @Param("delFlag") Integer delFlag,
                    @Param("updateBy") String updateBy);

    /**
     * 更新仓库状态
     * 
     * 业务逻辑：
     * 1. 更新warehouse_status字段
     * 2. 仅对仓库类型节点有效
     * 
     * @param nodeId 节点ID
     * @param warehouseStatus 仓库状态
     * @param updateBy 操作人ID
     */
    void updateWarehouseStatus(@Param("nodeId") Long nodeId,
                               @Param("warehouseStatus") Integer warehouseStatus,
                               @Param("updateBy") String updateBy);

    /**
     * 更新科室状态
     * 
     * 业务逻辑：
     * 1. 更新dept_status字段
     * 2. 仅对科室类型节点有效
     * 
     * @param nodeId 节点ID
     * @param deptStatus 科室状态
     * @param updateBy 操作人ID
     */
    void updateDeptStatus(@Param("nodeId") Long nodeId,
                          @Param("deptStatus") Integer deptStatus,
                          @Param("updateBy") String updateBy);
}
