package net.lab1024.sa.admin.module.spd.supplier.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.spd.supplier.domain.entity.SpdSupplierEntity;
import net.lab1024.sa.admin.module.spd.supplier.domain.form.SpdSupplierQueryForm;
import net.lab1024.sa.admin.module.spd.supplier.domain.vo.SpdSupplierVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SpdSupplierDao extends BaseMapper<SpdSupplierEntity> {

    Page<SpdSupplierVO> queryPage(Page page, @Param("query") SpdSupplierQueryForm query, @Param("delFlag") Integer delFlag);

    SpdSupplierVO getDetailById(@Param("id") Long id, @Param("delFlag") Integer delFlag);

    List<SpdSupplierVO> queryAll(@Param("delFlag") Integer delFlag);
}
