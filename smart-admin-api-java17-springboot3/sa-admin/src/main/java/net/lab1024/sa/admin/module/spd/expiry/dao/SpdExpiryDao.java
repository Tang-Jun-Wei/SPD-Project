package net.lab1024.sa.admin.module.spd.expiry.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.spd.expiry.domain.form.SpdExpiryWarningQueryForm;
import net.lab1024.sa.admin.module.spd.expiry.domain.vo.SpdExpiryStatisticsVO;
import net.lab1024.sa.admin.module.spd.expiry.domain.vo.SpdExpiryWarningVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 库存效期DAO
 *
 * @author 1024创新实验室
 */
@Mapper
public interface SpdExpiryDao {

    /**
     * 查询效期统计
     */
    SpdExpiryStatisticsVO selectStatistics();

    /**
     * 分页查询效期预警列表
     */
    Page<SpdExpiryWarningVO> queryWarningPage(Page page, @Param("query") SpdExpiryWarningQueryForm queryForm);
}
