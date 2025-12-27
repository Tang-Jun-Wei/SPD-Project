package net.lab1024.sa.admin.module.spd.warehouseuser.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.spd.warehouseuser.dao.SpdWarehouseUserDao;
import net.lab1024.sa.admin.module.spd.warehouseuser.domain.entity.SpdWarehouseUserEntity;
import net.lab1024.sa.admin.module.spd.warehouseuser.domain.form.SpdWarehouseUserForm;
import net.lab1024.sa.admin.module.spd.warehouseuser.domain.form.SpdWarehouseUserQueryForm;
import net.lab1024.sa.admin.module.spd.warehouseuser.domain.vo.SpdWarehouseUserVO;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
public class SpdWarehouseUserService {

    @Resource
    private SpdWarehouseUserDao spdWarehouseUserDao;

    /**
     * 分页查询仓库人员关系
     */
    public ResponseDTO<PageResult<SpdWarehouseUserVO>> queryPage(SpdWarehouseUserQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        Page<SpdWarehouseUserVO> pageData = spdWarehouseUserDao.queryPage(page, queryForm, 0);
        PageResult<SpdWarehouseUserVO> pageResult = SmartPageUtil.convert2PageResult(page, pageData.getRecords());
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 查询详情
     */
    public ResponseDTO<SpdWarehouseUserVO> getDetail(Long id) {
        SpdWarehouseUserVO vo = spdWarehouseUserDao.getDetailById(id);
        if (vo == null) {
            return ResponseDTO.userErrorParam("仓库人员关系不存在");
        }
        return ResponseDTO.ok(vo);
    }

    /**
     * 新增仓库人员关系
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> add(SpdWarehouseUserForm form, String loginUserId, String tenantId) {
        // 检查是否已存在关系
        Long count = spdWarehouseUserDao.selectCount(
            new LambdaQueryWrapper<SpdWarehouseUserEntity>()
                .eq(SpdWarehouseUserEntity::getWarehouseId, form.getWarehouseId())
                .eq(SpdWarehouseUserEntity::getUserId, form.getUserId())
                .eq(SpdWarehouseUserEntity::getDelFlag, 0)
        );
        if (count > 0) {
            return ResponseDTO.userErrorParam("该用户在该仓库的关系已存在");
        }

        // 生成业务ID
        String relationId = generateRelationId();

        // 创建实体
        SpdWarehouseUserEntity entity = SmartBeanUtil.copy(form, SpdWarehouseUserEntity.class);
        entity.setRelationId(relationId);
        entity.setStatus(form.getStatus() != null ? form.getStatus() : 1);
        entity.setTenantId(tenantId);
        entity.setDelFlag(0);
        entity.setCreateBy(loginUserId);
        entity.setCreateTime(LocalDateTime.now());

        spdWarehouseUserDao.insert(entity);

        log.info("新增仓库人员关系成功，relationId={}, warehouseId={}, userId={}", 
            relationId, form.getWarehouseId(), form.getUserId());

        return ResponseDTO.ok("新增成功");
    }

    /**
     * 更新仓库人员关系
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> update(SpdWarehouseUserForm form, String loginUserId) {
        if (form.getId() == null) {
            return ResponseDTO.userErrorParam("ID不能为空");
        }

        SpdWarehouseUserEntity entity = spdWarehouseUserDao.selectById(form.getId());
        if (entity == null || entity.getDelFlag() == 1) {
            return ResponseDTO.userErrorParam("仓库人员关系不存在");
        }

        // 更新字段
        entity.setRoleType(form.getRoleType());
        entity.setStatus(form.getStatus());
        entity.setRemark(form.getRemark());
        entity.setUpdateBy(loginUserId);
        entity.setUpdateTime(LocalDateTime.now());

        spdWarehouseUserDao.updateById(entity);

        log.info("更新仓库人员关系成功，relationId={}", entity.getRelationId());

        return ResponseDTO.ok("更新成功");
    }

    /**
     * 删除仓库人员关系
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> delete(Long id, String loginUserId) {
        SpdWarehouseUserEntity entity = spdWarehouseUserDao.selectById(id);
        if (entity == null || entity.getDelFlag() == 1) {
            return ResponseDTO.userErrorParam("仓库人员关系不存在");
        }

        entity.setDelFlag(1);
        entity.setUpdateBy(loginUserId);
        entity.setUpdateTime(LocalDateTime.now());

        spdWarehouseUserDao.updateById(entity);

        log.info("删除仓库人员关系成功，relationId={}", entity.getRelationId());

        return ResponseDTO.ok("删除成功");
    }

    /**
     * 更新状态
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> updateStatus(Long id, Integer status, String loginUserId) {
        SpdWarehouseUserEntity entity = spdWarehouseUserDao.selectById(id);
        if (entity == null || entity.getDelFlag() == 1) {
            return ResponseDTO.userErrorParam("仓库人员关系不存在");
        }

        entity.setStatus(status);
        entity.setUpdateBy(loginUserId);
        entity.setUpdateTime(LocalDateTime.now());

        spdWarehouseUserDao.updateById(entity);

        log.info("更新仓库人员关系状态成功，relationId={}, status={}", entity.getRelationId(), status);

        return ResponseDTO.ok("状态更新成功");
    }

    /**
     * 根据仓库ID查询人员列表
     */
    public ResponseDTO<List<SpdWarehouseUserVO>> queryByWarehouse(String warehouseId) {
        List<SpdWarehouseUserVO> list = spdWarehouseUserDao.queryByWarehouse(warehouseId, 0);
        return ResponseDTO.ok(list);
    }

    /**
     * 根据用户ID查询仓库列表
     */
    public ResponseDTO<List<SpdWarehouseUserVO>> queryByUser(String userId) {
        List<SpdWarehouseUserVO> list = spdWarehouseUserDao.queryByUser(userId, 0);
        return ResponseDTO.ok(list);
    }

    /**
     * 生成关系业务ID
     */
    private String generateRelationId() {
        String prefix = "REL";
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        Long count = spdWarehouseUserDao.selectCount(
            new LambdaQueryWrapper<SpdWarehouseUserEntity>()
                .likeRight(SpdWarehouseUserEntity::getRelationId, prefix + dateStr)
        );

        int newSeq = (count != null ? count.intValue() : 0) + 1;
        return prefix + dateStr + String.format("%06d", newSeq);
    }
}
