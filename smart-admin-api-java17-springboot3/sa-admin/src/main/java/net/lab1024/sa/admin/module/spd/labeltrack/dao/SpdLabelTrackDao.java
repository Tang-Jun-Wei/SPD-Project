package net.lab1024.sa.admin.module.spd.labeltrack.dao;

import net.lab1024.sa.admin.module.spd.labeltrack.domain.vo.SpdLabelTrackDetailVO;
import net.lab1024.sa.admin.module.spd.labeltrack.domain.vo.SpdLabelTrackVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 标签追溯DAO
 *
 * @author 1024创新实验室
 */
@Mapper
public interface SpdLabelTrackDao {

    /**
     * 根据标签编码查询标签基本信息
     */
    SpdLabelTrackVO selectByLabelCode(@Param("labelCode") String labelCode);

    /**
     * 查询标签轨迹列表
     */
    List<SpdLabelTrackDetailVO> selectTrackList(@Param("labelCode") String labelCode);
}
