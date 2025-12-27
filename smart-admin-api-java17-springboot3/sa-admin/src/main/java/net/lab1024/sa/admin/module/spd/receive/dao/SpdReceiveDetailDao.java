package net.lab1024.sa.admin.module.spd.receive.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.spd.receive.domain.entity.SpdReceiveDetailEntity;
import net.lab1024.sa.admin.module.spd.receive.domain.vo.SpdReceiveDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SpdReceiveDetailDao extends BaseMapper<SpdReceiveDetailEntity> {
    List<SpdReceiveDetailVO> queryByReceiveCode(@Param("receiveCode") String receiveCode, @Param("delFlag") Integer delFlag);
}
