package net.lab1024.sa.admin.module.spd.unpack.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.spd.unpack.domain.entity.SpdPackageUnpackEntity;
import net.lab1024.sa.admin.module.spd.unpack.domain.form.SpdPackageUnpackQueryForm;
import net.lab1024.sa.admin.module.spd.unpack.domain.vo.SpdPackageUnpackVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SpdPackageUnpackDao extends BaseMapper<SpdPackageUnpackEntity> {
    Page<SpdPackageUnpackVO> queryPage(Page page, @Param("query") SpdPackageUnpackQueryForm queryForm);
}
