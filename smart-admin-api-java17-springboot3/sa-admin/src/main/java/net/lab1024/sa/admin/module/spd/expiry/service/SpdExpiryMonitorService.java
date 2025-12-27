package net.lab1024.sa.admin.module.spd.expiry.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.spd.expiry.dao.SpdExpiryMonitorDao;
import net.lab1024.sa.admin.module.spd.expiry.domain.form.SpdExpiryMonitorQueryForm;
import net.lab1024.sa.admin.module.spd.expiry.domain.vo.SpdExpiryMonitorVO;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SpdExpiryMonitorService {

    @Resource
    private SpdExpiryMonitorDao spdExpiryMonitorDao;

    /**
     * 分页查询库存效期监控数据
     */
    public ResponseDTO<PageResult<SpdExpiryMonitorVO>> queryPage(SpdExpiryMonitorQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        Page<SpdExpiryMonitorVO> pageData = spdExpiryMonitorDao.queryPage(page, queryForm);
        PageResult<SpdExpiryMonitorVO> pageResult = SmartPageUtil.convert2PageResult(page, pageData.getRecords());
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 查询即将过期的库存（用于首页仪表盘或预警通知）
     * @param days 天数，例如30表示查询30天内到期的库存
     */
    public ResponseDTO<List<SpdExpiryMonitorVO>> queryExpiringSoon(Integer days) {
        if (days == null || days <= 0) {
            days = 30; // 默认30天
        }
        List<SpdExpiryMonitorVO> list = spdExpiryMonitorDao.queryExpiringSoon(days);
        return ResponseDTO.ok(list);
    }
}
