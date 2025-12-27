package net.lab1024.sa.admin.module.spd.location.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.spd.location.domain.entity.SpdLocationEntity;
import net.lab1024.sa.admin.module.spd.location.domain.vo.SpdLocationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SpdLocationDao extends BaseMapper<SpdLocationEntity> {
    Page<SpdLocationVO> queryPage(Page<SpdLocationVO> page, @Param("form") Object form);
}
