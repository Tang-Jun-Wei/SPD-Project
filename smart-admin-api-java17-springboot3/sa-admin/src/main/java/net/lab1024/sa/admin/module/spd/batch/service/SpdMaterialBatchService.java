package net.lab1024.sa.admin.module.spd.batch.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.spd.batch.dao.SpdMaterialBatchDao;
import net.lab1024.sa.admin.module.spd.batch.domain.entity.SpdMaterialBatchEntity;
import net.lab1024.sa.admin.module.spd.batch.domain.form.SpdMaterialBatchForm;
import net.lab1024.sa.admin.module.spd.batch.domain.form.SpdMaterialBatchQueryForm;
import net.lab1024.sa.admin.module.spd.batch.domain.vo.SpdMaterialBatchVO;
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

@Slf4j
@Service
public class SpdMaterialBatchService {

    @Autowired
    private SpdMaterialBatchDao spdMaterialBatchDao;

    public ResponseDTO<PageResult<SpdMaterialBatchVO>> queryPage(SpdMaterialBatchQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<SpdMaterialBatchVO> list = spdMaterialBatchDao.queryPage(page, queryForm);
        PageResult<SpdMaterialBatchVO> pageResult = SmartPageUtil.convert2PageResult(page, list);
        return ResponseDTO.ok(pageResult);
    }

    public ResponseDTO<SpdMaterialBatchVO> getDetail(Long id) {
        if (id == null) {
            return ResponseDTO.userErrorParam("批号ID不能为空");
        }
        SpdMaterialBatchVO vo = spdMaterialBatchDao.getDetail(id);
        if (vo == null) {
            return ResponseDTO.userErrorParam("批号不存在");
        }
        return ResponseDTO.ok(vo);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> add(SpdMaterialBatchForm form, String loginUserId, String tenantId) {
        SpdMaterialBatchEntity existEntity = spdMaterialBatchDao.queryByMaterialAndBatch(
            form.getMaterialId(), form.getBatchNo(), null);
        if (existEntity != null) {
            return ResponseDTO.userErrorParam("该耗材的批号已存在");
        }

        String batchId = generateBatchId();
        SpdMaterialBatchEntity entity = SmartBeanUtil.copy(form, SpdMaterialBatchEntity.class);
        entity.setBatchId(batchId);
        entity.setTenantId(tenantId);
        entity.setDelFlag(0);
        entity.setCreateBy(loginUserId);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateBy(loginUserId);
        entity.setUpdateTime(LocalDateTime.now());

        spdMaterialBatchDao.insert(entity);
        log.info("新增批号成功，batchId={}", batchId);
        return ResponseDTO.ok("新增成功");
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> update(SpdMaterialBatchForm form, String loginUserId) {
        if (form.getId() == null) {
            return ResponseDTO.userErrorParam("批号ID不能为空");
        }

        SpdMaterialBatchEntity existEntity = spdMaterialBatchDao.selectById(form.getId());
        if (existEntity == null || existEntity.getDelFlag() == 1) {
            return ResponseDTO.userErrorParam("批号不存在");
        }

        SpdMaterialBatchEntity duplicateEntity = spdMaterialBatchDao.queryByMaterialAndBatch(
            form.getMaterialId(), form.getBatchNo(), form.getId());
        if (duplicateEntity != null) {
            return ResponseDTO.userErrorParam("该耗材的批号已存在");
        }

        SpdMaterialBatchEntity updateEntity = SmartBeanUtil.copy(form, SpdMaterialBatchEntity.class);
        updateEntity.setUpdateBy(loginUserId);
        updateEntity.setUpdateTime(LocalDateTime.now());
        spdMaterialBatchDao.updateById(updateEntity);

        log.info("编辑批号成功，batchId={}", existEntity.getBatchId());
        return ResponseDTO.ok("编辑成功");
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> delete(Long id, String loginUserId) {
        if (id == null) {
            return ResponseDTO.userErrorParam("批号ID不能为空");
        }

        SpdMaterialBatchEntity entity = spdMaterialBatchDao.selectById(id);
        if (entity == null || entity.getDelFlag() == 1) {
            return ResponseDTO.userErrorParam("批号不存在");
        }

        entity.setDelFlag(1);
        entity.setUpdateBy(loginUserId);
        entity.setUpdateTime(LocalDateTime.now());
        spdMaterialBatchDao.updateById(entity);

        log.info("删除批号成功，batchId={}", entity.getBatchId());
        return ResponseDTO.ok("删除成功");
    }

    private String generateBatchId() {
        String prefix = "BATCH";
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        Long count = spdMaterialBatchDao.selectCount(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SpdMaterialBatchEntity>()
                .likeRight(SpdMaterialBatchEntity::getBatchId, prefix + dateStr)
        );
        int newSeq = (count != null ? count.intValue() : 0) + 1;
        return prefix + dateStr + String.format("%06d", newSeq);
    }
}
