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

    public PageResult<SpdStockVO> queryPage(SpdStockQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<SpdStockVO> list = spdStockDao.queryPage(page, queryForm, 0);
        return SmartPageUtil.convert2PageResult(page, list);
    }
}
