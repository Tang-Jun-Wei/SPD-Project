package net.lab1024.sa.admin.module.spd.bulkpackage.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.spd.bulkpackage.domain.entity.SpdBulkPackageEntity;
import net.lab1024.sa.admin.module.spd.bulkpackage.domain.form.SpdBulkPackageQueryForm;
import net.lab1024.sa.admin.module.spd.bulkpackage.domain.vo.SpdBulkPackageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 散货打包DAO
 */
@Mapper
public interface SpdBulkPackageDao extends BaseMapper<SpdBulkPackageEntity> {

    Page<SpdBulkPackageVO> queryPage(Page page, @Param("query") SpdBulkPackageQueryForm queryForm);
}
