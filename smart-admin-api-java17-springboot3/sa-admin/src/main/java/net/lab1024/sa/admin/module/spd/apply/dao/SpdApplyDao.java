package net.lab1024.sa.admin.module.spd.apply.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.spd.apply.entity.SpdApplyEntity;
import net.lab1024.sa.admin.module.spd.apply.domain.form.SpdApplyQueryForm;
import net.lab1024.sa.admin.module.spd.apply.domain.vo.SpdApplyVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SpdApplyDao extends BaseMapper<SpdApplyEntity> {

    List<SpdApplyVO> queryPage(Page<?> page, @Param("query") SpdApplyQueryForm queryForm, @Param("delFlag") Integer delFlag);

    SpdApplyVO getDetailById(@Param("id") Long id, @Param("delFlag") Integer delFlag);
}
