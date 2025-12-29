package net.lab1024.sa.admin.module.spd.category.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.spd.category.dao.SpdMaterialCategoryDao;
import net.lab1024.sa.admin.module.spd.category.domain.entity.SpdMaterialCategoryEntity;
import net.lab1024.sa.admin.module.spd.category.domain.form.SpdMaterialCategoryForm;
import net.lab1024.sa.admin.module.spd.category.domain.vo.SpdMaterialCategoryVO;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 耗材分类Service
 */
@Slf4j
@Service
public class SpdMaterialCategoryService {

    @Autowired
    private SpdMaterialCategoryDao categoryDao;

    /**
     * 查询树形分类列表
     */
    public ResponseDTO<List<SpdMaterialCategoryVO>> queryTree() {
        List<SpdMaterialCategoryEntity> allList = categoryDao.queryAll();
        List<SpdMaterialCategoryVO> voList = SmartBeanUtil.copyList(allList, SpdMaterialCategoryVO.class);
        
        // 构建树形结构
        List<SpdMaterialCategoryVO> treeList = buildTree(voList, 0L);
        return ResponseDTO.ok(treeList);
    }

    /**
     * 构建树形结构
     */
    private List<SpdMaterialCategoryVO> buildTree(List<SpdMaterialCategoryVO> allList, Long parentId) {
        List<SpdMaterialCategoryVO> result = new ArrayList<>();
        for (SpdMaterialCategoryVO vo : allList) {
            if (vo.getParentId().equals(parentId)) {
                List<SpdMaterialCategoryVO> children = buildTree(allList, vo.getId());
                if (!children.isEmpty()) {
                    vo.setChildren(children);
                }
                result.add(vo);
            }
        }
        return result;
    }

    /**
     * 新增分类
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> add(SpdMaterialCategoryForm form, Long userId) {
        // 校验父分类
        if (form.getParentId() > 0) {
            SpdMaterialCategoryEntity parent = categoryDao.selectById(form.getParentId());
            if (parent == null) {
                return ResponseDTO.userErrorParam("父分类不存在");
            }
            if (parent.getLevel() >= 3) {
                return ResponseDTO.userErrorParam("最多支持3级分类");
            }
        }

        SpdMaterialCategoryEntity entity = SmartBeanUtil.copy(form, SpdMaterialCategoryEntity.class);
        
        // 计算层级
        if (form.getParentId() == 0) {
            entity.setLevel(1);
        } else {
            SpdMaterialCategoryEntity parent = categoryDao.selectById(form.getParentId());
            entity.setLevel(parent.getLevel() + 1);
        }
        
        entity.setCreateUserId(userId);
        entity.setCreateTime(LocalDateTime.now());
        entity.setStatus(1);
        entity.setDeletedFlag(0);
        
        categoryDao.insert(entity);
        return ResponseDTO.ok();
    }

    /**
     * 修改分类
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> update(SpdMaterialCategoryForm form, Long userId) {
        SpdMaterialCategoryEntity entity = categoryDao.selectById(form.getId());
        if (entity == null) {
            return ResponseDTO.userErrorParam("分类不存在");
        }

        // 不允许修改父分类为自己或自己的子分类
        if (form.getParentId().equals(form.getId())) {
            return ResponseDTO.userErrorParam("父分类不能是自己");
        }

        SmartBeanUtil.copyProperties(form, entity);
        entity.setUpdateUserId(userId);
        entity.setUpdateTime(LocalDateTime.now());
        
        categoryDao.updateById(entity);
        return ResponseDTO.ok();
    }

    /**
     * 删除分类
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> delete(Long id, Long userId) {
        SpdMaterialCategoryEntity entity = categoryDao.selectById(id);
        if (entity == null) {
            return ResponseDTO.userErrorParam("分类不存在");
        }

        // 检查是否有子分类
        int childCount = categoryDao.countChildren(id);
        if (childCount > 0) {
            return ResponseDTO.userErrorParam("存在子分类,无法删除");
        }

        // 逻辑删除
        entity.setDeletedFlag(1);
        entity.setUpdateUserId(userId);
        entity.setUpdateTime(LocalDateTime.now());
        categoryDao.updateById(entity);
        
        return ResponseDTO.ok();
    }

    /**
     * 更新状态
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> updateStatus(Long id, Integer status, Long userId) {
        SpdMaterialCategoryEntity entity = categoryDao.selectById(id);
        if (entity == null) {
            return ResponseDTO.userErrorParam("分类不存在");
        }

        entity.setStatus(status);
        entity.setUpdateUserId(userId);
        entity.setUpdateTime(LocalDateTime.now());
        categoryDao.updateById(entity);
        
        return ResponseDTO.ok();
    }

    /**
     * 查询所有启用的分类(下拉选项用)
     */
    public ResponseDTO<List<SpdMaterialCategoryVO>> queryAllEnabled() {
        LambdaQueryWrapper<SpdMaterialCategoryEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SpdMaterialCategoryEntity::getStatus, 1);
        wrapper.orderByAsc(SpdMaterialCategoryEntity::getSort);
        
        List<SpdMaterialCategoryEntity> list = categoryDao.selectList(wrapper);
        List<SpdMaterialCategoryVO> voList = SmartBeanUtil.copyList(list, SpdMaterialCategoryVO.class);
        return ResponseDTO.ok(voList);
    }
}
