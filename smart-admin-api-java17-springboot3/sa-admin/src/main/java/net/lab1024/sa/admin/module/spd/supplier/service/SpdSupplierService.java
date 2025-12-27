package net.lab1024.sa.admin.module.spd.supplier.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.spd.supplier.dao.SpdSupplierDao;
import net.lab1024.sa.admin.module.spd.supplier.domain.entity.SpdSupplierEntity;
import net.lab1024.sa.admin.module.spd.supplier.domain.form.SpdSupplierForm;
import net.lab1024.sa.admin.module.spd.supplier.domain.form.SpdSupplierQueryForm;
import net.lab1024.sa.admin.module.spd.supplier.domain.vo.SpdSupplierVO;
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
public class SpdSupplierService {

    @Resource
    private SpdSupplierDao spdSupplierDao;

    public ResponseDTO<PageResult<SpdSupplierVO>> queryPage(SpdSupplierQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        Page<SpdSupplierVO> result = spdSupplierDao.queryPage(page, queryForm, 0);
        PageResult<SpdSupplierVO> pageResult = SmartPageUtil.convert2PageResult(page, result.getRecords());
        return ResponseDTO.ok(pageResult);
    }

    public ResponseDTO<SpdSupplierVO> getDetail(Long id) {
        SpdSupplierVO detail = spdSupplierDao.getDetailById(id, 0);
        if (detail == null) {
            return ResponseDTO.userErrorParam("供应商不存在");
        }
        return ResponseDTO.ok(detail);
    }

    public ResponseDTO<List<SpdSupplierVO>> queryAll() {
        List<SpdSupplierVO> list = spdSupplierDao.queryAll(0);
        return ResponseDTO.ok(list);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> add(SpdSupplierForm form, String loginUserId, String tenantId) {
        String supplierId = generateSupplierId();
        
        SpdSupplierEntity entity = SmartBeanUtil.copy(form, SpdSupplierEntity.class);
        entity.setSupplierId(supplierId);
        entity.setCreateBy(loginUserId);
        entity.setCreateTime(LocalDateTime.now());
        entity.setTenantId(tenantId);
        entity.setDelFlag(0);
        if (entity.getStatus() == null) {
            entity.setStatus(1);
        }
        spdSupplierDao.insert(entity);
        log.info("新增供应商成功，supplierId={}, supplierName={}", supplierId, form.getSupplierName());
        return ResponseDTO.ok("新增成功");
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> update(SpdSupplierForm form, String loginUserId) {
        SpdSupplierEntity entity = spdSupplierDao.selectById(form.getId());
        if (entity == null || entity.getDelFlag() == 1) {
            return ResponseDTO.userErrorParam("供应商不存在");
        }
        SmartBeanUtil.copyProperties(form, entity);
        entity.setUpdateBy(loginUserId);
        entity.setUpdateTime(LocalDateTime.now());
        spdSupplierDao.updateById(entity);
        log.info("更新供应商成功，id={}", form.getId());
        return ResponseDTO.ok("更新成功");
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> delete(Long id, String loginUserId) {
        SpdSupplierEntity entity = spdSupplierDao.selectById(id);
        if (entity == null || entity.getDelFlag() == 1) {
            return ResponseDTO.userErrorParam("供应商不存在");
        }
        entity.setDelFlag(1);
        entity.setUpdateBy(loginUserId);
        entity.setUpdateTime(LocalDateTime.now());
        spdSupplierDao.updateById(entity);
        log.info("删除供应商成功，id={}", id);
        return ResponseDTO.ok("删除成功");
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> updateStatus(Long id, Integer status, String loginUserId) {
        SpdSupplierEntity entity = spdSupplierDao.selectById(id);
        if (entity == null || entity.getDelFlag() == 1) {
            return ResponseDTO.userErrorParam("供应商不存在");
        }
        entity.setStatus(status);
        entity.setUpdateBy(loginUserId);
        entity.setUpdateTime(LocalDateTime.now());
        spdSupplierDao.updateById(entity);
        log.info("更新供应商状态成功，id={}，status={}", id, status);
        return ResponseDTO.ok("更新成功");
    }

    private String generateSupplierId() {
        String prefix = "SUP";
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        Long count = spdSupplierDao.selectCount(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SpdSupplierEntity>()
                .likeRight(SpdSupplierEntity::getSupplierId, prefix + dateStr)
        );
        int newSeq = (count != null ? count.intValue() : 0) + 1;
        return prefix + dateStr + String.format("%06d", newSeq);
    }
}
