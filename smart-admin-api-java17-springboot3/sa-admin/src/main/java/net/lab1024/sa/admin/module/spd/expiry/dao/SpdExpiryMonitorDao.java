package net.lab1024.sa.admin.module.spd.expiry.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.spd.expiry.domain.form.SpdExpiryMonitorQueryForm;
import net.lab1024.sa.admin.module.spd.expiry.domain.vo.SpdExpiryMonitorVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SpdExpiryMonitorDao {

    /**
     * 分页查询库存效期监控数据
     */
    Page<SpdExpiryMonitorVO> queryPage(Page page, @Param("query") SpdExpiryMonitorQueryForm query);

    /**
     * 查询即将过期的库存统计（用于首页仪表盘）
     */
    List<SpdExpiryMonitorVO> queryExpiringSoon(@Param("days") Integer days);
}
