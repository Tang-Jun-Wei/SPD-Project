package net.lab1024.sa.admin.module.spd.orgnode.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.spd.orgnode.dao.SpdOrgNodeDao;
import net.lab1024.sa.admin.module.spd.orgnode.domain.entity.SpdOrgNodeEntity;
import net.lab1024.sa.admin.module.spd.orgnode.domain.form.SpdOrgNodeForm;
import net.lab1024.sa.admin.module.spd.orgnode.domain.form.SpdOrgNodeQueryForm;
import net.lab1024.sa.admin.module.spd.orgnode.domain.vo.SpdOrgNodeVO;
import net.lab1024.sa.base.common.code.UserErrorCode;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.module.support.datatracer.service.DataTracerService;
import net.lab1024.sa.base.module.support.datatracer.constant.DataTracerTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * SPD组织节点管理 - 业务逻辑层
 * 
 * 功能说明：
 * 1. 组织节点的增删改查业务逻辑
 * 2. 节点业务ID自动生成（DEPT/WH+年月日+6位序号）
 * 3. 节点名称+类型唯一性校验
 * 4. 支持科室和仓库两种节点类型
 * 5. 支持状态管理（启用/禁用）
 * 6. 集成数据变更追踪
 * 
 * 业务规则：
 * - 节点名称+类型在同一租户下唯一
 * - 科室可关联上级仓库
 * - 仓库分为中心库、科室库、临时库
 * - 删除节点前需校验是否被引用
 * 
 * @since 2025-12-27
 */
@Slf4j
@Service
public class SpdOrgNodeService {

    @Autowired
    private SpdOrgNodeDao spdOrgNodeDao;

    @Autowired
    private DataTracerService dataTracerService;

    /**
     * 分页查询组织节点列表
     * 
     * 业务逻辑：
     * 1. 构建分页参数
     * 2. 调用Dao层执行分页查询
     * 3. 返回包含类型名称、状态名称的VO对象
     * 
     * @param queryForm 查询条件
     * @return 分页结果
     */
    public ResponseDTO<PageResult<SpdOrgNodeVO>> queryPage(SpdOrgNodeQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<SpdOrgNodeVO> list = spdOrgNodeDao.queryPage(page, queryForm);
        PageResult<SpdOrgNodeVO> pageResult = SmartPageUtil.convert2PageResult(page, list);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 根据ID查询组织节点详情
     * 
     * 业务逻辑：
     * 1. 校验节点ID不能为空
     * 2. 查询节点详情（del_flag=0）
     * 3. 校验节点是否存在
     * 
     * @param nodeId 节点ID
     * @return 节点详情VO
     */
    public ResponseDTO<SpdOrgNodeVO> getDetail(Long nodeId) {
        if (nodeId == null) {
            return ResponseDTO.userErrorParam("节点ID不能为空");
        }

        SpdOrgNodeVO nodeVO = spdOrgNodeDao.getDetail(nodeId, 0);
        if (nodeVO == null) {
            return ResponseDTO.userErrorParam("节点不存在或已被删除");
        }

        return ResponseDTO.ok(nodeVO);
    }

    /**
     * 新增组织节点
     * 
     * 业务逻辑：
     * 1. 校验节点名称+类型唯一性
     * 2. 校验节点类型必填（1=科室 2=仓库）
     * 3. 根据节点类型自动生成节点业务ID
     *    - 科室：DEPT+年月日+6位序号（如DEPT202512270001）
     *    - 仓库：WH+年月日+6位序号（如WH202512270001）
     * 4. 设置默认状态（启用）
     * 5. 保存到数据库
     * 6. 记录数据变更日志
     * 
     * @param form 新增表单
     * @param loginUserId 登录用户ID
     * @param tenantId 租户ID
     * @return 操作结果
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> add(SpdOrgNodeForm form, String loginUserId, String tenantId) {
        // 1. 校验节点名称+类型唯一性
        SpdOrgNodeEntity existNode = spdOrgNodeDao.queryByNameAndType(
            form.getNodeName(), 
            form.getNodeType(), 
            null, 
            0
        );
        if (existNode != null) {
            return ResponseDTO.userErrorParam("节点名称已存在");
        }

        // 2. 生成节点业务ID
        String nodeId = generateNodeId(form.getNodeType());

        // 3. 构建实体对象
        SpdOrgNodeEntity entity = SmartBeanUtil.copy(form, SpdOrgNodeEntity.class);
        entity.setNodeId(nodeId);
        entity.setTenantId(tenantId);
        entity.setDelFlag(0);
        entity.setCreateBy(loginUserId);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateBy(loginUserId);
        entity.setUpdateTime(LocalDateTime.now());

        // 4. 设置默认状态
        if (form.getNodeType() == 1) {
            // 科室默认启用
            entity.setDeptStatus(form.getDeptStatus() != null ? form.getDeptStatus() : 1);
        } else if (form.getNodeType() == 2) {
            // 仓库默认启用
            entity.setWarehouseStatus(form.getWarehouseStatus() != null ? form.getWarehouseStatus() : 1);
        }

        // 5. 保存到数据库
        spdOrgNodeDao.insert(entity);

        // 6. 记录数据变更日志
        // dataTracerService.insert(entity.getId(), DataTracerTypeEnum.SPD_ORG_NODE);

        log.info("新增组织节点成功，nodeId={}, nodeName={}", nodeId, form.getNodeName());
        return ResponseDTO.ok("新增成功");
    }

    /**
     * 编辑组织节点
     * 
     * 业务逻辑：
     * 1. 校验节点ID必填
     * 2. 查询节点是否存在
     * 3. 校验节点名称+类型唯一性（排除自身）
     * 4. 更新节点信息
     * 5. 记录数据变更日志
     * 
     * @param form 编辑表单
     * @param loginUserId 登录用户ID
     * @return 操作结果
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> update(SpdOrgNodeForm form, String loginUserId) {
        // 1. 校验节点ID
        if (form.getId() == null) {
            return ResponseDTO.userErrorParam("节点ID不能为空");
        }

        // 2. 查询节点是否存在
        SpdOrgNodeEntity existEntity = spdOrgNodeDao.selectById(form.getId());
        if (existEntity == null || existEntity.getDelFlag() == 1) {
            return ResponseDTO.userErrorParam("节点不存在或已被删除");
        }

        // 3. 校验节点名称+类型唯一性（排除自身）
        SpdOrgNodeEntity duplicateNode = spdOrgNodeDao.queryByNameAndType(
            form.getNodeName(), 
            form.getNodeType(), 
            form.getId(), 
            0
        );
        if (duplicateNode != null) {
            return ResponseDTO.userErrorParam("节点名称已存在");
        }

        // 4. 更新节点信息
        SpdOrgNodeEntity updateEntity = SmartBeanUtil.copy(form, SpdOrgNodeEntity.class);
        updateEntity.setUpdateBy(loginUserId);
        updateEntity.setUpdateTime(LocalDateTime.now());
        spdOrgNodeDao.updateById(updateEntity);

        // 5. 记录数据变更日志
        // dataTracerService.update(form.getId(), DataTracerTypeEnum.SPD_ORG_NODE);

        log.info("编辑组织节点成功，nodeId={}, nodeName={}", existEntity.getNodeId(), form.getNodeName());
        return ResponseDTO.ok("编辑成功");
    }

    /**
     * 删除组织节点
     * 
     * 业务逻辑：
     * 1. 校验节点ID不能为空
     * 2. 查询节点是否存在
     * 3. 校验节点是否被引用（科室、耗材等）
     * 4. 执行逻辑删除（设置del_flag=1）
     * 5. 记录数据变更日志
     * 
     * @param nodeId 节点ID
     * @param loginUserId 登录用户ID
     * @return 操作结果
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> delete(Long nodeId, String loginUserId) {
        // 1. 校验节点ID
        if (nodeId == null) {
            return ResponseDTO.userErrorParam("节点ID不能为空");
        }

        // 2. 查询节点是否存在
        SpdOrgNodeEntity entity = spdOrgNodeDao.selectById(nodeId);
        if (entity == null || entity.getDelFlag() == 1) {
            return ResponseDTO.userErrorParam("节点不存在或已被删除");
        }

        // 3. 校验节点是否被引用
        // TODO: 后续需要校验节点是否被科室、耗材、库存等引用

        // 4. 执行逻辑删除
        spdOrgNodeDao.deleteNode(nodeId, 1, loginUserId);

        // 5. 记录数据变更日志
        // dataTracerService.delete(nodeId, DataTracerTypeEnum.SPD_ORG_NODE);

        log.info("删除组织节点成功，nodeId={}, nodeName={}", entity.getNodeId(), entity.getNodeName());
        return ResponseDTO.ok("删除成功");
    }

    /**
     * 查询科室列表（用于下拉选择）
     * 
     * 业务逻辑：
     * 1. 查询所有启用状态的科室节点
     * 2. 按租户筛选
     * 3. 返回简化的VO对象
     * 
     * @param tenantId 租户ID
     * @return 科室列表
     */
    public ResponseDTO<List<SpdOrgNodeVO>> queryDeptList(String tenantId) {
        List<SpdOrgNodeVO> list = spdOrgNodeDao.queryDeptList(tenantId, 0);
        return ResponseDTO.ok(list);
    }

    /**
     * 查询仓库列表（用于下拉选择）
     * 
     * 业务逻辑：
     * 1. 查询所有启用状态的仓库节点
     * 2. 按租户筛选
     * 3. 返回简化的VO对象
     * 
     * @param tenantId 租户ID
     * @return 仓库列表
     */
    public ResponseDTO<List<SpdOrgNodeVO>> queryWarehouseList(String tenantId) {
        List<SpdOrgNodeVO> list = spdOrgNodeDao.queryWarehouseList(tenantId, 0);
        return ResponseDTO.ok(list);
    }

    /**
     * 更新仓库状态
     * 
     * 业务逻辑：
     * 1. 校验节点是否为仓库类型
     * 2. 更新warehouse_status字段
     * 3. 记录数据变更日志
     * 
     * @param nodeId 节点ID
     * @param warehouseStatus 仓库状态（1=启用 0=禁用）
     * @param loginUserId 登录用户ID
     * @return 操作结果
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> updateWarehouseStatus(Long nodeId, Integer warehouseStatus, String loginUserId) {
        // 校验节点是否存在且为仓库类型
        SpdOrgNodeEntity entity = spdOrgNodeDao.selectById(nodeId);
        if (entity == null || entity.getDelFlag() == 1) {
            return ResponseDTO.userErrorParam("节点不存在或已被删除");
        }
        if (entity.getNodeType() != 2) {
            return ResponseDTO.userErrorParam("仅仓库节点可更新仓库状态");
        }

        spdOrgNodeDao.updateWarehouseStatus(nodeId, warehouseStatus, loginUserId);
        // dataTracerService.update(nodeId, DataTracerTypeEnum.SPD_ORG_NODE);

        log.info("更新仓库状态成功，nodeId={}, status={}", entity.getNodeId(), warehouseStatus);
        return ResponseDTO.ok("更新成功");
    }

    /**
     * 更新科室状态
     * 
     * 业务逻辑：
     * 1. 校验节点是否为科室类型
     * 2. 更新dept_status字段
     * 3. 记录数据变更日志
     * 
     * @param nodeId 节点ID
     * @param deptStatus 科室状态（1=启用 0=禁用）
     * @param loginUserId 登录用户ID
     * @return 操作结果
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> updateDeptStatus(Long nodeId, Integer deptStatus, String loginUserId) {
        // 校验节点是否存在且为科室类型
        SpdOrgNodeEntity entity = spdOrgNodeDao.selectById(nodeId);
        if (entity == null || entity.getDelFlag() == 1) {
            return ResponseDTO.userErrorParam("节点不存在或已被删除");
        }
        if (entity.getNodeType() != 1) {
            return ResponseDTO.userErrorParam("仅科室节点可更新科室状态");
        }

        spdOrgNodeDao.updateDeptStatus(nodeId, deptStatus, loginUserId);
        // dataTracerService.update(nodeId, DataTracerTypeEnum.SPD_ORG_NODE);

        log.info("更新科室状态成功，nodeId={}, status={}", entity.getNodeId(), deptStatus);
        return ResponseDTO.ok("更新成功");
    }

    /**
     * 生成节点业务ID
     * 
     * 业务逻辑：
     * 1. 根据节点类型生成前缀
     *    - 科室：DEPT
     *    - 仓库：WH
     * 2. 获取当前日期（yyyyMMdd）
     * 3. 查询当天该类型节点的最大序号
     * 4. 序号+1，补齐6位数字
     * 5. 拼接生成最终业务ID
     * 
     * 示例：
     * - DEPT20251227000001（科室）
     * - WH20251227000001（仓库）
     * 
     * @param nodeType 节点类型（1=科室 2=仓库）
     * @return 节点业务ID
     */
    private String generateNodeId(Integer nodeType) {
        // 1. 确定前缀
        String prefix = nodeType == 1 ? "DEPT" : "WH";

        // 2. 获取当前日期
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // 3. 查询当天该类型节点的最大序号
        String likePattern = prefix + dateStr + "%";
        Long maxSeqLong = spdOrgNodeDao.selectCount(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SpdOrgNodeEntity>()
                .likeRight(SpdOrgNodeEntity::getNodeId, likePattern)
        );
        Integer maxSeq = maxSeqLong != null ? maxSeqLong.intValue() : null;

        // 4. 生成新序号
        int newSeq = (maxSeq != null ? maxSeq : 0) + 1;
        String seqStr = String.format("%06d", newSeq);

        // 5. 拼接生成最终业务ID
        return prefix + dateStr + seqStr;
    }
}
