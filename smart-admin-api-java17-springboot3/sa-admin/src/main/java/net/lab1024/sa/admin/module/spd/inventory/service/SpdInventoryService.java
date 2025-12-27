package net.lab1024.sa.admin.module.spd.inventory.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.spd.inventory.dao.SpdInventoryDao;
import net.lab1024.sa.admin.module.spd.inventory.domain.form.SpdInventoryQueryForm;
import net.lab1024.sa.admin.module.spd.inventory.domain.vo.SpdInventoryVO;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class SpdInventoryService {

    @Autowired
    private SpdInventoryDao spdInventoryDao;

    public ResponseDTO<PageResult<SpdInventoryVO>> queryPage(SpdInventoryQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<SpdInventoryVO> list = spdInventoryDao.queryPage(page, queryForm);
        PageResult<SpdInventoryVO> pageResult = SmartPageUtil.convert2PageResult(page, list);
        return ResponseDTO.ok(pageResult);
    }

    public ResponseDTO<SpdInventoryVO> getDetail(Long id) {
        if (id == null) {
            return ResponseDTO.userErrorParam("库存ID不能为空");
        }
        SpdInventoryVO vo = spdInventoryDao.getDetail(id);
        if (vo == null) {
            return ResponseDTO.userErrorParam("库存记录不存在");
        }
        return ResponseDTO.ok(vo);
    }
}
