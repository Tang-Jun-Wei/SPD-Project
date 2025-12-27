package net.lab1024.sa.admin.module.spd.bulk.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.spd.bulk.entity.SpdBulkMaterialEntity;
import net.lab1024.sa.admin.module.spd.bulk.domain.form.SpdBulkMaterialQueryForm;
import net.lab1024.sa.admin.module.spd.bulk.domain.vo.SpdBulkMaterialVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SpdBulkMaterialDao extends BaseMapper<SpdBulkMaterialEntity> {
    List<SpdBulkMaterialVO> queryPage(Page<?> page, @Param("query") SpdBulkMaterialQueryForm queryForm, @Param("delFlag") Integer delFlag);
}
