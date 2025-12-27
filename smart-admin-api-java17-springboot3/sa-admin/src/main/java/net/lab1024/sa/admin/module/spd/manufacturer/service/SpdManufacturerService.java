package net.lab1024.sa.admin.module.spd.manufacturer.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.spd.manufacturer.dao.SpdManufacturerDao;
import net.lab1024.sa.admin.module.spd.manufacturer.domain.entity.SpdManufacturerEntity;
import net.lab1024.sa.admin.module.spd.manufacturer.domain.form.SpdManufacturerForm;
import net.lab1024.sa.admin.module.spd.manufacturer.domain.form.SpdManufacturerQueryForm;
import net.lab1024.sa.admin.module.spd.manufacturer.domain.vo.SpdManufacturerVO;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
public class SpdManufacturerService {

    @Resource
    private SpdManufacturerDao spdManufacturerDao;

    public ResponseDTO<PageResult<SpdManufacturerVO>> queryPage(SpdManufacturerQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        Page<SpdManufacturerVO> result = spdManufacturerDao.queryPage(page, queryForm, 0);
        PageResult<SpdManufacturerVO> pageResult = SmartPageUtil.convert2PageResult(page, result.getRecords());
        return ResponseDTO.ok(pageResult);
    }

    public ResponseDTO<SpdManufacturerVO> getDetail(Long id) {
        SpdManufacturerVO detail = spdManufacturerDao.getDetailById(id, 0);
        if (detail == null) {
            return ResponseDTO.userErrorParam("生产厂家不存在");
        }
        return ResponseDTO.ok(detail);
    }

    public ResponseDTO<List<SpdManufacturerVO>> queryAll() {
        List<SpdManufacturerVO> list = spdManufacturerDao.queryAll(0);
        return ResponseDTO.ok(list);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> add(SpdManufacturerForm form, String loginUserId, String tenantId) {
        String manufacturerId = generateManufacturerId();
        
        SpdManufacturerEntity entity = SmartBeanUtil.copy(form, SpdManufacturerEntity.class);
        entity.setManufacturerId(manufacturerId);
        entity.setCreateBy(loginUserId);
        entity.setCreateTime(LocalDateTime.now());
        entity.setTenantId(tenantId);
        entity.setDelFlag(0);
        if (entity.getStatus() == null) {
            entity.setStatus(1);
        }
        spdManufacturerDao.insert(entity);
        log.info("新增生产厂家成功，manufacturerId={}, manufacturerName={}", manufacturerId, form.getManufacturerName());
        return ResponseDTO.ok("新增成功");
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> update(SpdManufacturerForm form, String loginUserId) {
        SpdManufacturerEntity entity = spdManufacturerDao.selectById(form.getId());
        if (entity == null || entity.getDelFlag() == 1) {
            return ResponseDTO.userErrorParam("生产厂家不存在");
        }
        SmartBeanUtil.copyProperties(form, entity);
        entity.setUpdateBy(loginUserId);
        entity.setUpdateTime(LocalDateTime.now());
        spdManufacturerDao.updateById(entity);
        log.info("更新生产厂家成功，id={}", form.getId());
        return ResponseDTO.ok("更新成功");
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> delete(Long id, String loginUserId) {
        SpdManufacturerEntity entity = spdManufacturerDao.selectById(id);
        if (entity == null || entity.getDelFlag() == 1) {
            return ResponseDTO.userErrorParam("生产厂家不存在");
        }
        entity.setDelFlag(1);
        entity.setUpdateBy(loginUserId);
        entity.setUpdateTime(LocalDateTime.now());
        spdManufacturerDao.updateById(entity);
        log.info("删除生产厂家成功，id={}", id);
        return ResponseDTO.ok("删除成功");
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> updateStatus(Long id, Integer status, String loginUserId) {
        SpdManufacturerEntity entity = spdManufacturerDao.selectById(id);
        if (entity == null || entity.getDelFlag() == 1) {
            return ResponseDTO.userErrorParam("生产厂家不存在");
        }
        entity.setStatus(status);
        entity.setUpdateBy(loginUserId);
        entity.setUpdateTime(LocalDateTime.now());
        spdManufacturerDao.updateById(entity);
        log.info("更新生产厂家状态成功，id={}，status={}", id, status);
        return ResponseDTO.ok("更新成功");
    }

    private String generateManufacturerId() {
        String prefix = "MFR";
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        Long count = spdManufacturerDao.selectCount(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SpdManufacturerEntity>()
                .likeRight(SpdManufacturerEntity::getManufacturerId, prefix + dateStr)
        );
        int newSeq = (count != null ? count.intValue() : 0) + 1;
        return prefix + dateStr + String.format("%06d", newSeq);
    }
}
