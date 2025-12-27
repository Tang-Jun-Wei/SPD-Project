package net.lab1024.sa.admin.module.spd.spdpackage.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.spd.spdpackage.domain.entity.SpdPackageMainEntity;
import net.lab1024.sa.admin.module.spd.spdpackage.domain.form.SpdPackageQueryForm;
import net.lab1024.sa.admin.module.spd.spdpackage.domain.vo.SpdPackageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SpdPackageDao extends BaseMapper<SpdPackageMainEntity> {
    List<SpdPackageVO> queryPage(@Param("page") Page<?> page, @Param("query") SpdPackageQueryForm queryForm, @Param("delFlag") Integer delFlag);
    
    SpdPackageVO getDetail(@Param("packageId") String packageId, @Param("delFlag") Integer delFlag);
}
