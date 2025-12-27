package net.lab1024.sa.admin.module.spd.acceptance.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.sa.admin.module.spd.acceptance.entity.SpdAcceptanceDetailEntity;
import net.lab1024.sa.admin.module.spd.acceptance.domain.vo.SpdAcceptanceDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 验收明细 Dao
 */
@Mapper
public interface SpdAcceptanceDetailDao extends BaseMapper<SpdAcceptanceDetailEntity> {
    
    /**
     * 根据验收ID查询明细列表
     */
    List<SpdAcceptanceDetailVO> queryByAcceptanceId(@Param("acceptanceId") String acceptanceId,
                                                     @Param("delFlag") Integer delFlag);
}
