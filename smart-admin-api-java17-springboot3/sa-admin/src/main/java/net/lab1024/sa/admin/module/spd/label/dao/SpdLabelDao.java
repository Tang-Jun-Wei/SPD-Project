package net.lab1024.sa.admin.module.spd.label.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.spd.label.entity.SpdLabelEntity;
import net.lab1024.sa.admin.module.spd.label.domain.form.SpdLabelQueryForm;
import net.lab1024.sa.admin.module.spd.label.domain.vo.SpdLabelVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SpdLabelDao extends BaseMapper<SpdLabelEntity> {
    List<SpdLabelVO> queryPage(Page<?> page, @Param("query") SpdLabelQueryForm queryForm, @Param("delFlag") Integer delFlag);
    
    SpdLabelVO getDetailByCode(@Param("labelCode") String labelCode, @Param("delFlag") Integer delFlag);
}
