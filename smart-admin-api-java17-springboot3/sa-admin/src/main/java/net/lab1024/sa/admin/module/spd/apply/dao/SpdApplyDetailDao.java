package net.lab1024.sa.admin.module.spd.apply.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.sa.admin.module.spd.apply.entity.SpdApplyDetailEntity;
import net.lab1024.sa.admin.module.spd.apply.domain.vo.SpdApplyDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SpdApplyDetailDao extends BaseMapper<SpdApplyDetailEntity> {

    List<SpdApplyDetailVO> queryByApplyId(@Param("applyId") String applyId, @Param("delFlag") Integer delFlag);
}
