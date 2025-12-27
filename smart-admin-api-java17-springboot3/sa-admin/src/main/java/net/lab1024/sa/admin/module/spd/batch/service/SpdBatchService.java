package net.lab1024.sa.admin.module.spd.batch.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.spd.batch.dao.SpdBatchDao;
import net.lab1024.sa.admin.module.spd.batch.domain.form.SpdBatchForm;
import net.lab1024.sa.admin.module.spd.batch.domain.form.SpdBatchQueryForm;
import net.lab1024.sa.admin.module.spd.batch.domain.vo.SpdBatchVO;
import net.lab1024.sa.admin.module.spd.batch.entity.SpdBatchEntity;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 批号信息Service
 */
@Slf4j
@Service
public class SpdBatchService {

    @Autowired
    private SpdBatchDao spdBatchDao;

    /**
     * 分页查询
     */
    public PageResult<SpdBatchVO> queryPage(SpdBatchQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<SpdBatchVO> list = spdBatchDao.queryPage(page, queryForm, 0);
        return SmartPageUtil.convert2PageResult(page, list);
    }

    /**
     * 新增批号
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> add(SpdBatchForm form, String loginUserId, String tenantId) {
        // 1. 校验批号+耗材ID唯一性
        SpdBatchEntity existBatch = spdBatchDao.queryByBatchNoAndMaterialId(
            form.getBatchNo(), form.getMaterialId(), null, 0);
        if (existBatch != null) {
            return ResponseDTO.userErrorParam("该耗材的批号已存在");
        }

        // 2. 生成批号业务ID
        String batchId = generateBatchId();

        // 3. 构建实体对象
        SpdBatchEntity entity = SmartBeanUtil.copy(form, SpdBatchEntity.class);
        entity.setBatchId(batchId);
        entity.setTenantId(tenantId);
        entity.setDelFlag(0);
        entity.setBatchStatus(form.getBatchStatus() != null ? form.getBatchStatus() : 1);
        entity.setCreateBy(loginUserId);
        entity.setCreateTime(LocalDateTime.now());

        // 4. 保存到数据库
        spdBatchDao.insert(entity);

        log.info("新增批号成功，batchId={}", batchId);
        return ResponseDTO.ok("新增成功");
    }

    /**
     * 修改批号
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> update(SpdBatchForm form, String loginUserId) {
        // 1. 校验是否存在
        if (form.getId() == null) {
            return ResponseDTO.userErrorParam("ID不能为空");
        }
        
        SpdBatchEntity existEntity = spdBatchDao.selectById(form.getId());
        if (existEntity == null || existEntity.getDelFlag() == 1) {
            return ResponseDTO.userErrorParam("批号不存在");
        }

        // 2. 校验批号+耗材ID唯一性
        SpdBatchEntity duplicateBatch = spdBatchDao.queryByBatchNoAndMaterialId(
            form.getBatchNo(), form.getMaterialId(), form.getId(), 0);
        if (duplicateBatch != null) {
            return ResponseDTO.userErrorParam("该耗材的批号已存在");
        }

        // 3. 更新实体对象
        SpdBatchEntity entity = SmartBeanUtil.copy(form, SpdBatchEntity.class);
        entity.setId(form.getId());
        entity.setUpdateBy(loginUserId);
        entity.setUpdateTime(LocalDateTime.now());

        spdBatchDao.updateById(entity);

        log.info("修改批号成功，id={}", form.getId());
        return ResponseDTO.ok("修改成功");
    }

    /**
     * 删除批号（逻辑删除）
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> delete(Long id, String loginUserId) {
        SpdBatchEntity entity = spdBatchDao.selectById(id);
        if (entity == null || entity.getDelFlag() == 1) {
            return ResponseDTO.userErrorParam("批号不存在");
        }

        entity.setDelFlag(1);
        entity.setUpdateBy(loginUserId);
        entity.setUpdateTime(LocalDateTime.now());
        spdBatchDao.updateById(entity);

        log.info("删除批号成功，id={}", id);
        return ResponseDTO.ok("删除成功");
    }

    /**
     * 生成批号业务ID
     * 格式：BAT + 年月日 + 6位序号
     */
    private String generateBatchId() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        Long count = spdBatchDao.selectCount(
            new LambdaQueryWrapper<SpdBatchEntity>()
                .likeRight(SpdBatchEntity::getBatchId, "BAT" + dateStr)
        );
        int newSeq = (count != null ? count.intValue() : 0) + 1;
        return "BAT" + dateStr + String.format("%06d", newSeq);
    }
}
