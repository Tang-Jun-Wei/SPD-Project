package net.lab1024.sa.admin.module.spd.warehouseuser.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.spd.warehouseuser.domain.entity.SpdWarehouseUserEntity;
import net.lab1024.sa.admin.module.spd.warehouseuser.domain.form.SpdWarehouseUserQueryForm;
import net.lab1024.sa.admin.module.spd.warehouseuser.domain.vo.SpdWarehouseUserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SpdWarehouseUserDao extends BaseMapper<SpdWarehouseUserEntity> {

    /**
     * 分页查询仓库人员关系
     */
    Page<SpdWarehouseUserVO> queryPage(Page page, @Param("query") SpdWarehouseUserQueryForm query, @Param("delFlag") Integer delFlag);

    /**
     * 查询详情
     */
    SpdWarehouseUserVO getDetailById(@Param("id") Long id);

    /**
     * 根据仓库ID查询人员列表
     */
    List<SpdWarehouseUserVO> queryByWarehouse(@Param("warehouseId") String warehouseId, @Param("delFlag") Integer delFlag);

    /**
     * 根据用户ID查询仓库列表
     */
    List<SpdWarehouseUserVO> queryByUser(@Param("userId") String userId, @Param("delFlag") Integer delFlag);
}
