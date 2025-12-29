package net.lab1024.sa.admin.module.spd.unit.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.spd.unit.dao.SpdUnitConversionDao;
import net.lab1024.sa.admin.module.spd.unit.dao.SpdUnitDao;
import net.lab1024.sa.admin.module.spd.unit.domain.entity.SpdUnitConversionEntity;
import net.lab1024.sa.admin.module.spd.unit.domain.entity.SpdUnitEntity;
import net.lab1024.sa.admin.module.spd.unit.domain.form.SpdUnitConversionForm;
import net.lab1024.sa.admin.module.spd.unit.domain.form.SpdUnitConversionQueryForm;
import net.lab1024.sa.admin.module.spd.unit.domain.vo.SpdUnitConversionVO;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class SpdUnitConversionService {

    @Autowired
    private SpdUnitConversionDao conversionDao;
    
    @Autowired
    private SpdUnitDao unitDao;

    public ResponseDTO<List<SpdUnitConversionVO>> queryList(SpdUnitConversionQueryForm queryForm) {
        LambdaQueryWrapper<SpdUnitConversionEntity> wrapper = new LambdaQueryWrapper<>();
        if (queryForm.getFromUnitId() != null) {
            wrapper.eq(SpdUnitConversionEntity::getFromUnitId, queryForm.getFromUnitId());
        }
        if (queryForm.getToUnitId() != null) {
            wrapper.eq(SpdUnitConversionEntity::getToUnitId, queryForm.getToUnitId());
        }
        if (queryForm.getStatus() != null) {
            wrapper.eq(SpdUnitConversionEntity::getStatus, queryForm.getStatus());
        }
        wrapper.orderByDesc(SpdUnitConversionEntity::getCreateTime);

        List<SpdUnitConversionEntity> list = conversionDao.selectList(wrapper);
        List<SpdUnitConversionVO> voList = SmartBeanUtil.copyList(list, SpdUnitConversionVO.class);
        return ResponseDTO.ok(voList);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> add(SpdUnitConversionForm form, Long userId) {
        // 获取单位名称
        SpdUnitEntity fromUnit = unitDao.selectById(form.getFromUnitId());
        SpdUnitEntity toUnit = unitDao.selectById(form.getToUnitId());
        if (fromUnit == null || toUnit == null) {
            return ResponseDTO.userErrorParam("单位不存在");
        }

        SpdUnitConversionEntity entity = SmartBeanUtil.copy(form, SpdUnitConversionEntity.class);
        entity.setFromUnitName(fromUnit.getUnitName());
        entity.setToUnitName(toUnit.getUnitName());
        entity.setCreateUserId(userId);
        entity.setCreateTime(LocalDateTime.now());
        entity.setStatus(1);
        entity.setDeletedFlag(0);
        conversionDao.insert(entity);
        return ResponseDTO.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> update(SpdUnitConversionForm form, Long userId) {
        SpdUnitConversionEntity entity = conversionDao.selectById(form.getId());
        if (entity == null) {
            return ResponseDTO.userErrorParam("换算关系不存在");
        }

        // 更新单位名称
        if (!entity.getFromUnitId().equals(form.getFromUnitId()) || !entity.getToUnitId().equals(form.getToUnitId())) {
            SpdUnitEntity fromUnit = unitDao.selectById(form.getFromUnitId());
            SpdUnitEntity toUnit = unitDao.selectById(form.getToUnitId());
            if (fromUnit == null || toUnit == null) {
                return ResponseDTO.userErrorParam("单位不存在");
            }
            entity.setFromUnitName(fromUnit.getUnitName());
            entity.setToUnitName(toUnit.getUnitName());
        }

        SmartBeanUtil.copyProperties(form, entity);
        entity.setUpdateUserId(userId);
        entity.setUpdateTime(LocalDateTime.now());
        conversionDao.updateById(entity);
        return ResponseDTO.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> delete(Long id, Long userId) {
        SpdUnitConversionEntity entity = conversionDao.selectById(id);
        if (entity == null) {
            return ResponseDTO.userErrorParam("换算关系不存在");
        }

        entity.setDeletedFlag(1);
        entity.setUpdateUserId(userId);
        entity.setUpdateTime(LocalDateTime.now());
        conversionDao.updateById(entity);
        return ResponseDTO.ok();
    }
}
