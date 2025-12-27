package net.lab1024.sa.admin.module.spd.consume.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.sa.admin.module.spd.consume.domain.entity.SpdConsumeMainEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SpdConsumeDao extends BaseMapper<SpdConsumeMainEntity> {
}
