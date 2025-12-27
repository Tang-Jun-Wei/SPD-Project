package net.lab1024.sa.admin.module.spd.spdpackage.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.sa.admin.module.spd.spdpackage.domain.entity.SpdPackageMainEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SpdPackageDao extends BaseMapper<SpdPackageMainEntity> {
}
