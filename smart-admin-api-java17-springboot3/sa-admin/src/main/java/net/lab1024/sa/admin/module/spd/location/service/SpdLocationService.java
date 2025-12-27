package net.lab1024.sa.admin.module.spd.location.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.spd.location.dao.SpdLocationDao;
import net.lab1024.sa.admin.module.spd.location.domain.entity.SpdLocationEntity;
import net.lab1024.sa.admin.module.spd.location.domain.form.SpdLocationForm;
import net.lab1024.sa.admin.module.spd.location.domain.vo.SpdLocationVO;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class SpdLocationService {

    @Resource
    private SpdLocationDao spdLocationDao;

    public ResponseDTO<PageResult<SpdLocationVO>> queryPage(Object form) {
        Page<SpdLocationVO> page = SmartPageUtil.convert2PageQuery(form);
        Page<SpdLocationVO> pageResult = spdLocationDao.queryPage(page, form);
        PageResult<SpdLocationVO> result = SmartPageUtil.convert2PageResult(pageResult);
        return ResponseDTO.ok(result);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> add(SpdLocationForm form, String loginUserId, String tenantId) {
        String locationId = generateLocationId();
        
        SpdLocationEntity entity = SmartBeanUtil.copy(form, SpdLocationEntity.class);
        entity.setLocationId(locationId);
        entity.setTenantId(tenantId);
        entity.setCurrentQuantity(0);
        entity.setCreateBy(loginUserId);
        entity.setCreateTime(LocalDateTime.now());
        
        if (entity.getLocationStatus() == null) {
            entity.setLocationStatus(1);
        }
        
        spdLocationDao.insert(entity);
        return ResponseDTO.ok("新增成功");
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> update(SpdLocationForm form, String loginUserId) {
        SpdLocationEntity entity = spdLocationDao.selectOne(
                new LambdaQueryWrapper<SpdLocationEntity>()
                        .eq(SpdLocationEntity::getLocationId, form.getLocationId())
        );
        
        if (entity == null) {
            return ResponseDTO.userErrorParam("货位不存在");
        }
        
        entity.setLocationCode(form.getLocationCode());
        entity.setLocationName(form.getLocationName());
        entity.setNodeId(form.getNodeId());
        entity.setLocationLevel(form.getLocationLevel());
        entity.setLocationType(form.getLocationType());
        entity.setMaxCapacity(form.getMaxCapacity());
        entity.setLocationStatus(form.getLocationStatus());
        entity.setRemark(form.getRemark());
        entity.setUpdateBy(loginUserId);
        entity.setUpdateTime(LocalDateTime.now());
        
        spdLocationDao.updateById(entity);
        return ResponseDTO.ok("更新成功");
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> delete(String locationId, String loginUserId) {
        SpdLocationEntity entity = spdLocationDao.selectOne(
                new LambdaQueryWrapper<SpdLocationEntity>()
                        .eq(SpdLocationEntity::getLocationId, locationId)
        );
        
        if (entity == null) {
            return ResponseDTO.userErrorParam("货位不存在");
        }
        
        if (entity.getCurrentQuantity() > 0) {
            return ResponseDTO.userErrorParam("该货位仍有库存，无法删除");
        }
        
        entity.setDelFlag(1);
        entity.setUpdateBy(loginUserId);
        entity.setUpdateTime(LocalDateTime.now());
        spdLocationDao.updateById(entity);
        
        return ResponseDTO.ok("删除成功");
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> updateStatus(String locationId, Integer status, String loginUserId) {
        SpdLocationEntity entity = spdLocationDao.selectOne(
                new LambdaQueryWrapper<SpdLocationEntity>()
                        .eq(SpdLocationEntity::getLocationId, locationId)
        );
        
        if (entity == null) {
            return ResponseDTO.userErrorParam("货位不存在");
        }
        
        entity.setLocationStatus(status);
        entity.setUpdateBy(loginUserId);
        entity.setUpdateTime(LocalDateTime.now());
        spdLocationDao.updateById(entity);
        
        return ResponseDTO.ok("状态更新成功");
    }

    private String generateLocationId() {
        String prefix = "LOC";
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        Long count = spdLocationDao.selectCount(
                new LambdaQueryWrapper<SpdLocationEntity>()
                        .likeRight(SpdLocationEntity::getLocationId, prefix + dateStr)
        );
        int newSeq = (count != null ? count.intValue() : 0) + 1;
        return prefix + dateStr + String.format("%06d", newSeq);
    }
}
