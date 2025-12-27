package net.lab1024.sa.admin.module.spd.acceptance.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.sa.admin.module.spd.acceptance.domain.entity.SpdAcceptanceMainEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SpdAcceptanceDao extends BaseMapper<SpdAcceptanceMainEntity> {
}
