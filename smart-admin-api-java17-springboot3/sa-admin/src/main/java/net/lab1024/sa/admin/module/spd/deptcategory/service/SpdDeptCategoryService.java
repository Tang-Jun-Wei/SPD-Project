package net.lab1024.sa.admin.module.spd.deptcategory.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.spd.deptcategory.dao.SpdDeptCategoryDao;
import net.lab1024.sa.admin.module.spd.deptcategory.domain.entity.SpdDeptCategoryEntity;
import net.lab1024.sa.admin.module.spd.deptcategory.domain.form.SpdDeptCategoryForm;
import net.lab1024.sa.admin.module.spd.deptcategory.domain.vo.SpdDeptCategoryTreeVO;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SpdDeptCategoryService {

    @Resource
    private SpdDeptCategoryDao spdDeptCategoryDao;

    public ResponseDTO<List<SpdDeptCategoryTreeVO>> queryTree(String tenantId) {
        List<SpdDeptCategoryEntity> allCategories = spdDeptCategoryDao.selectList(
                new LambdaQueryWrapper<SpdDeptCategoryEntity>()
                        .eq(SpdDeptCategoryEntity::getTenantId, tenantId)
                        .eq(SpdDeptCategoryEntity::getDelFlag, 0)
                        .orderByAsc(SpdDeptCategoryEntity::getSortOrder)
        );

        List<SpdDeptCategoryTreeVO> treeList = buildTree(allCategories, null);
        return ResponseDTO.ok(treeList);
    }

    private List<SpdDeptCategoryTreeVO> buildTree(List<SpdDeptCategoryEntity> allCategories, String parentId) {
        List<SpdDeptCategoryTreeVO> treeList = new ArrayList<>();
        
        for (SpdDeptCategoryEntity entity : allCategories) {
            boolean isMatch = (parentId == null && entity.getParentId() == null) ||
                    (parentId != null && parentId.equals(entity.getParentId()));
            
            if (isMatch) {
                SpdDeptCategoryTreeVO vo = SmartBeanUtil.copy(entity, SpdDeptCategoryTreeVO.class);
                List<SpdDeptCategoryTreeVO> children = buildTree(allCategories, entity.getCategoryId());
                vo.setChildren(children);
                vo.setHasChildren(!children.isEmpty());
                treeList.add(vo);
            }
        }
        
        return treeList;
    }

    public ResponseDTO<List<SpdDeptCategoryTreeVO>> queryAll(String tenantId) {
        List<SpdDeptCategoryEntity> allCategories = spdDeptCategoryDao.selectList(
                new LambdaQueryWrapper<SpdDeptCategoryEntity>()
                        .eq(SpdDeptCategoryEntity::getTenantId, tenantId)
                        .eq(SpdDeptCategoryEntity::getDelFlag, 0)
                        .orderByAsc(SpdDeptCategoryEntity::getSortOrder)
        );

        List<SpdDeptCategoryTreeVO> result = allCategories.stream()
                .map(entity -> SmartBeanUtil.copy(entity, SpdDeptCategoryTreeVO.class))
                .collect(Collectors.toList());
        
        return ResponseDTO.ok(result);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> add(SpdDeptCategoryForm form, String loginUserId, String tenantId) {
        String categoryId = generateCategoryId();
        
        SpdDeptCategoryEntity entity = SmartBeanUtil.copy(form, SpdDeptCategoryEntity.class);
        entity.setCategoryId(categoryId);
        entity.setTenantId(tenantId);
        entity.setCreateBy(loginUserId);
        entity.setCreateTime(LocalDateTime.now());
        
        if (entity.getCategoryLevel() == null) {
            if (StringUtils.isBlank(entity.getParentId())) {
                entity.setCategoryLevel(1);
            } else {
                SpdDeptCategoryEntity parent = spdDeptCategoryDao.selectOne(
                        new LambdaQueryWrapper<SpdDeptCategoryEntity>()
                                .eq(SpdDeptCategoryEntity::getCategoryId, entity.getParentId())
                );
                entity.setCategoryLevel(parent != null ? parent.getCategoryLevel() + 1 : 1);
            }
        }
        
        if (entity.getSortOrder() == null) {
            entity.setSortOrder(0);
        }
        
        if (entity.getCategoryStatus() == null) {
            entity.setCategoryStatus(1);
        }
        
        spdDeptCategoryDao.insert(entity);
        return ResponseDTO.ok("新增成功");
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> update(SpdDeptCategoryForm form, String loginUserId) {
        SpdDeptCategoryEntity entity = spdDeptCategoryDao.selectOne(
                new LambdaQueryWrapper<SpdDeptCategoryEntity>()
                        .eq(SpdDeptCategoryEntity::getCategoryId, form.getCategoryId())
        );
        
        if (entity == null) {
            return ResponseDTO.userErrorParam("分类不存在");
        }
        
        entity.setCategoryName(form.getCategoryName());
        entity.setCategoryCode(form.getCategoryCode());
        entity.setSortOrder(form.getSortOrder());
        entity.setCategoryStatus(form.getCategoryStatus());
        entity.setRemark(form.getRemark());
        entity.setUpdateBy(loginUserId);
        entity.setUpdateTime(LocalDateTime.now());
        
        spdDeptCategoryDao.updateById(entity);
        return ResponseDTO.ok("更新成功");
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> delete(String categoryId, String loginUserId) {
        SpdDeptCategoryEntity entity = spdDeptCategoryDao.selectOne(
                new LambdaQueryWrapper<SpdDeptCategoryEntity>()
                        .eq(SpdDeptCategoryEntity::getCategoryId, categoryId)
        );
        
        if (entity == null) {
            return ResponseDTO.userErrorParam("分类不存在");
        }
        
        Long childCount = spdDeptCategoryDao.selectCount(
                new LambdaQueryWrapper<SpdDeptCategoryEntity>()
                        .eq(SpdDeptCategoryEntity::getParentId, categoryId)
                        .eq(SpdDeptCategoryEntity::getDelFlag, 0)
        );
        
        if (childCount > 0) {
            return ResponseDTO.userErrorParam("该分类下存在子分类，无法删除");
        }
        
        entity.setDelFlag(1);
        entity.setUpdateBy(loginUserId);
        entity.setUpdateTime(LocalDateTime.now());
        spdDeptCategoryDao.updateById(entity);
        
        return ResponseDTO.ok("删除成功");
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> updateStatus(String categoryId, Integer status, String loginUserId) {
        SpdDeptCategoryEntity entity = spdDeptCategoryDao.selectOne(
                new LambdaQueryWrapper<SpdDeptCategoryEntity>()
                        .eq(SpdDeptCategoryEntity::getCategoryId, categoryId)
        );
        
        if (entity == null) {
            return ResponseDTO.userErrorParam("分类不存在");
        }
        
        entity.setCategoryStatus(status);
        entity.setUpdateBy(loginUserId);
        entity.setUpdateTime(LocalDateTime.now());
        spdDeptCategoryDao.updateById(entity);
        
        return ResponseDTO.ok("状态更新成功");
    }

    private String generateCategoryId() {
        String prefix = "DEPTCAT";
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        Long count = spdDeptCategoryDao.selectCount(
                new LambdaQueryWrapper<SpdDeptCategoryEntity>()
                        .likeRight(SpdDeptCategoryEntity::getCategoryId, prefix + dateStr)
        );
        int newSeq = (count != null ? count.intValue() : 0) + 1;
        return prefix + dateStr + String.format("%06d", newSeq);
    }
}
