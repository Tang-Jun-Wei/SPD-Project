package net.lab1024.sa.admin.module.spd.deptconsume.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.sa.admin.module.spd.deptconsume.domain.entity.SpdDeptConsumeDetailEntity;
import net.lab1024.sa.admin.module.spd.deptconsume.domain.vo.SpdDeptConsumeDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 科室消耗明细DAO
 *
 * @author 1024创新实验室
 */
@Mapper
public interface SpdDeptConsumeDetailDao extends BaseMapper<SpdDeptConsumeDetailEntity> {

    /**
     * 根据消耗单ID查询明细列表
     */
    List<SpdDeptConsumeDetailVO> selectByConsumeId(@Param("consumeId") Long consumeId);
}
