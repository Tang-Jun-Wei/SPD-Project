package net.lab1024.sa.admin.module.spd.stock.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.spd.stock.dao.SpdStockDao;
import net.lab1024.sa.admin.module.spd.stock.domain.form.SpdStockQueryForm;
import net.lab1024.sa.admin.module.spd.stock.domain.vo.SpdStockVO;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class SpdStockService {
    @Autowired
    private SpdStockDao spdStockDao;

    /**
     * 分页查询库存
     */
    public PageResult<SpdStockVO> queryPage(SpdStockQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<SpdStockVO> list = spdStockDao.queryPage(page, queryForm, 0);
        return SmartPageUtil.convert2PageResult(page, list);
    }

    /**
     * 批号汇总查询（按批号聚合）
     * TODO: 待实现 - 需要添加SpdStockDao.queryBatchSummary方法
     */
    // public List<SpdStockVO> queryBatchSummary(SpdStockQueryForm queryForm) {
    //     return spdStockDao.queryBatchSummary(queryForm);
    // }

    /**
     * 查询批号明细
     * TODO: 待实现 - 需要添加SpdStockDao.queryBatchDetail方法
     */
    // public List<SpdStockVO> queryBatchDetail(String batchId, String warehouseId) {
    //     return spdStockDao.queryBatchDetail(batchId, warehouseId);
    // }
}
