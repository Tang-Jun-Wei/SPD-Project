package net.lab1024.sa.admin.module.spd.manufacturer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.spd.manufacturer.domain.entity.SpdManufacturerEntity;
import net.lab1024.sa.admin.module.spd.manufacturer.domain.form.SpdManufacturerQueryForm;
import net.lab1024.sa.admin.module.spd.manufacturer.domain.vo.SpdManufacturerVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SpdManufacturerDao extends BaseMapper<SpdManufacturerEntity> {

    Page<SpdManufacturerVO> queryPage(Page<?> page, @Param("query") SpdManufacturerQueryForm query, @Param("delFlag") Integer delFlag);

    SpdManufacturerVO getDetailById(@Param("id") Long id, @Param("delFlag") Integer delFlag);

    List<SpdManufacturerVO> queryAll(@Param("delFlag") Integer delFlag);
}
