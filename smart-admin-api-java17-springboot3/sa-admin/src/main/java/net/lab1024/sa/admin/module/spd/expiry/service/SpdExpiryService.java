package net.lab1024.sa.admin.module.spd.expiry.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.spd.expiry.dao.SpdExpiryDao;
import net.lab1024.sa.admin.module.spd.expiry.domain.form.SpdExpiryWarningQueryForm;
import net.lab1024.sa.admin.module.spd.expiry.domain.vo.SpdExpiryStatisticsVO;
import net.lab1024.sa.admin.module.spd.expiry.domain.vo.SpdExpiryWarningVO;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 库存效期Service
 *
 * @author 1024创新实验室
 */
@Slf4j
@Service
public class SpdExpiryService {

    @Autowired
    private SpdExpiryDao spdExpiryDao;

    /**
     * 查询效期统计
     */
    public ResponseDTO<SpdExpiryStatisticsVO> getStatistics() {
        SpdExpiryStatisticsVO statistics = spdExpiryDao.selectStatistics();
        return ResponseDTO.ok(statistics);
    }

    /**
     * 分页查询效期预警列表
     */
    public ResponseDTO<PageResult<SpdExpiryWarningVO>> queryWarningPage(SpdExpiryWarningQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        Page<SpdExpiryWarningVO> pageResult = spdExpiryDao.queryWarningPage(page, queryForm);
        PageResult<SpdExpiryWarningVO> pageResultVO = SmartPageUtil.convert2PageResult(pageResult);
        return ResponseDTO.ok(pageResultVO);
    }
}
