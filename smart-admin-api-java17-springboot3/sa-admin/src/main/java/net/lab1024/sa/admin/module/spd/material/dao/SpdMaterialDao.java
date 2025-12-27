package net.lab1024.sa.admin.module.spd.material.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.spd.material.entity.SpdMaterialEntity;
import net.lab1024.sa.admin.module.spd.material.domain.form.SpdMaterialQueryForm;
import net.lab1024.sa.admin.module.spd.material.domain.vo.SpdMaterialVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 耗材信息Dao接口
 */
@Mapper
public interface SpdMaterialDao extends BaseMapper<SpdMaterialEntity> {

    /**
     * 分页查询
     */
    List<SpdMaterialVO> queryPage(Page<?> page, @Param("query") SpdMaterialQueryForm queryForm, @Param("delFlag") Integer delFlag);

    /**
     * 根据耗材名称和生产厂家查询（用于唯一性校验）
     */
    SpdMaterialEntity queryByNameAndManufacturer(@Param("materialName") String materialName, 
                                                   @Param("manufacturer") String manufacturer,
                                                   @Param("id") Long id,
                                                   @Param("delFlag") Integer delFlag);

    /**
     * 根据业务ID查询
     */
    SpdMaterialEntity queryByMaterialId(@Param("materialId") String materialId, @Param("delFlag") Integer delFlag);
}
