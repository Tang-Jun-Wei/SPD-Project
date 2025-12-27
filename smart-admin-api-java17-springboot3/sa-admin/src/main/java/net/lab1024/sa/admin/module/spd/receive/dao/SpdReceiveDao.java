package net.lab1024.sa.admin.module.spd.receive.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.sa.admin.module.spd.receive.domain.entity.SpdReceiveMainEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SpdReceiveDao extends BaseMapper<SpdReceiveMainEntity> {
}
