package net.lab1024.sa.admin.module.spd.unit.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.spd.unit.dao.SpdUnitDao;
import net.lab1024.sa.admin.module.spd.unit.domain.entity.SpdUnitEntity;
import net.lab1024.sa.admin.module.spd.unit.domain.form.SpdUnitForm;
import net.lab1024.sa.admin.module.spd.unit.domain.form.SpdUnitQueryForm;
import net.lab1024.sa.admin.module.spd.unit.domain.vo.SpdUnitVO;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class SpdUnitService {

    @Autowired
    private SpdUnitDao unitDao;

    public ResponseDTO<List<SpdUnitVO>> queryPage(SpdUnitQueryForm queryForm) {
        LambdaQueryWrapper<SpdUnitEntity> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(queryForm.getUnitName())) {
            wrapper.like(SpdUnitEntity::getUnitName, queryForm.getUnitName());
        }
        if (queryForm.getUnitType() != null) {
            wrapper.eq(SpdUnitEntity::getUnitType, queryForm.getUnitType());
        }
        if (queryForm.getStatus() != null) {
            wrapper.eq(SpdUnitEntity::getStatus, queryForm.getStatus());
        }
        wrapper.orderByAsc(SpdUnitEntity::getSort);

        List<SpdUnitEntity> list = unitDao.selectList(wrapper);
        List<SpdUnitVO> voList = SmartBeanUtil.copyList(list, SpdUnitVO.class);
        return ResponseDTO.ok(voList);
    }

    public ResponseDTO<List<SpdUnitVO>> queryAll() {
        LambdaQueryWrapper<SpdUnitEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SpdUnitEntity::getStatus, 1);
        wrapper.orderByAsc(SpdUnitEntity::getSort);
        
        List<SpdUnitEntity> list = unitDao.selectList(wrapper);
        List<SpdUnitVO> voList = SmartBeanUtil.copyList(list, SpdUnitVO.class);
        return ResponseDTO.ok(voList);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> add(SpdUnitForm form, Long userId) {
        SpdUnitEntity entity = SmartBeanUtil.copy(form, SpdUnitEntity.class);
        entity.setCreateUserId(userId);
        entity.setCreateTime(LocalDateTime.now());
        entity.setStatus(1);
        entity.setDeletedFlag(0);
        unitDao.insert(entity);
        return ResponseDTO.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> update(SpdUnitForm form, Long userId) {
        SpdUnitEntity entity = unitDao.selectById(form.getId());
        if (entity == null) {
            return ResponseDTO.userErrorParam("单位不存在");
        }
        
        SmartBeanUtil.copyProperties(form, entity);
        entity.setUpdateUserId(userId);
        entity.setUpdateTime(LocalDateTime.now());
        unitDao.updateById(entity);
        return ResponseDTO.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> delete(Long id, Long userId) {
        SpdUnitEntity entity = unitDao.selectById(id);
        if (entity == null) {
            return ResponseDTO.userErrorParam("单位不存在");
        }
        
        entity.setDeletedFlag(1);
        entity.setUpdateUserId(userId);
        entity.setUpdateTime(LocalDateTime.now());
        unitDao.updateById(entity);
        return ResponseDTO.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> updateStatus(Long id, Integer status, Long userId) {
        SpdUnitEntity entity = unitDao.selectById(id);
        if (entity == null) {
            return ResponseDTO.userErrorParam("单位不存在");
        }
        
        entity.setStatus(status);
        entity.setUpdateUserId(userId);
        entity.setUpdateTime(LocalDateTime.now());
        unitDao.updateById(entity);
        return ResponseDTO.ok();
    }
}
