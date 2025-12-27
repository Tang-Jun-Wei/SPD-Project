package net.lab1024.sa.admin.module.spd.stocklimit.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.spd.stocklimit.domain.entity.SpdStockLimitEntity;
import net.lab1024.sa.admin.module.spd.stocklimit.domain.form.SpdStockLimitQueryForm;
import net.lab1024.sa.admin.module.spd.stocklimit.domain.vo.SpdStockLimitVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SpdStockLimitDao extends BaseMapper<SpdStockLimitEntity> {

    /**
     * 分页查询库存安全量配置
     */
    Page<SpdStockLimitVO> queryPage(Page page, @Param("query") SpdStockLimitQueryForm query, @Param("delFlag") Integer delFlag);

    /**
     * 查询详情
     */
    SpdStockLimitVO getDetailById(@Param("id") Long id);
}
