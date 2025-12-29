package net.lab1024.sa.admin.module.spd.category.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.sa.admin.module.spd.category.domain.entity.SpdMaterialCategoryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 耗材分类DAO
 */
@Mapper
public interface SpdMaterialCategoryDao extends BaseMapper<SpdMaterialCategoryEntity> {

    /**
     * 查询所有分类(树形结构使用)
     */
    @Select("SELECT * FROM spd_material_category WHERE deleted_flag = 0 ORDER BY parent_id, sort")
    List<SpdMaterialCategoryEntity> queryAll();

    /**
     * 查询子分类数量
     */
    @Select("SELECT COUNT(*) FROM spd_material_category WHERE parent_id = #{parentId} AND deleted_flag = 0")
    int countChildren(Long parentId);
}
