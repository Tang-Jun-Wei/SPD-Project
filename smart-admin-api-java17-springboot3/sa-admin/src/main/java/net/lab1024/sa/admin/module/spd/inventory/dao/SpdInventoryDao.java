package net.lab1024.sa.admin.module.spd.inventory.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.spd.inventory.domain.entity.SpdInventoryEntity;
import net.lab1024.sa.admin.module.spd.inventory.domain.form.SpdInventoryQueryForm;
import net.lab1024.sa.admin.module.spd.inventory.domain.vo.SpdInventoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SpdInventoryDao extends BaseMapper<SpdInventoryEntity> {
    List<SpdInventoryVO> queryPage(Page page, @Param("queryForm") SpdInventoryQueryForm queryForm);
    SpdInventoryVO getDetail(@Param("id") Long id);
}
