package net.lab1024.sa.admin.module.spd.supplierreturn.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.spd.supplierreturn.domain.entity.SpdSupplierReturnEntity;
import net.lab1024.sa.admin.module.spd.supplierreturn.domain.vo.SpdSupplierReturnVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SpdSupplierReturnDao extends BaseMapper<SpdSupplierReturnEntity> {
    /**
     * 分页查询退供应商单列表
     */
    Page<SpdSupplierReturnVO> queryPage(Page<SpdSupplierReturnVO> page, @Param("form") Object form);

    /**
     * 查询退供应商单详情
     */
    SpdSupplierReturnVO getDetail(@Param("supplierReturnId") String supplierReturnId);
}
