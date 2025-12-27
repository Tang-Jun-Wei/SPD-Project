package net.lab1024.sa.admin.module.spd.bulkpackage.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.sa.admin.module.spd.bulkpackage.domain.entity.SpdBulkPackageDetailEntity;
import net.lab1024.sa.admin.module.spd.bulkpackage.domain.vo.SpdBulkPackageDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 散货打包明细DAO
 */
@Mapper
public interface SpdBulkPackageDetailDao extends BaseMapper<SpdBulkPackageDetailEntity> {

    List<SpdBulkPackageDetailVO> selectByPackageId(@Param("packageId") Long packageId);
}
