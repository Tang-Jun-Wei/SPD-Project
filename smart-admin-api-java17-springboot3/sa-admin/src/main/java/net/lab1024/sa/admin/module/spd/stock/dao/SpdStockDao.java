package net.lab1024.sa.admin.module.spd.stock.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.spd.stock.entity.SpdStockEntity;
import net.lab1024.sa.admin.module.spd.stock.domain.form.SpdStockQueryForm;
import net.lab1024.sa.admin.module.spd.stock.domain.vo.SpdStockVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SpdStockDao extends BaseMapper<SpdStockEntity> {
    List<SpdStockVO> queryPage(Page<?> page, @Param("query") SpdStockQueryForm queryForm, @Param("delFlag") Integer delFlag);
}
