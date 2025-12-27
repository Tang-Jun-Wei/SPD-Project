package net.lab1024.sa.admin.module.spd.unpack.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.spd.unpack.dao.SpdPackageUnpackDao;
import net.lab1024.sa.admin.module.spd.unpack.domain.entity.SpdPackageUnpackEntity;
import net.lab1024.sa.admin.module.spd.unpack.domain.form.SpdPackageUnpackAddForm;
import net.lab1024.sa.admin.module.spd.unpack.domain.form.SpdPackageUnpackQueryForm;
import net.lab1024.sa.admin.module.spd.unpack.domain.vo.SpdPackageUnpackVO;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
public class SpdPackageUnpackService {

    @Autowired
    private SpdPackageUnpackDao spdPackageUnpackDao;

    public ResponseDTO<PageResult<SpdPackageUnpackVO>> queryPage(SpdPackageUnpackQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<SpdPackageUnpackVO> list = spdPackageUnpackDao.queryPage(page, queryForm).getRecords();
        return ResponseDTO.ok(SmartPageUtil.convert2PageResult(page, list));
    }

    public ResponseDTO<SpdPackageUnpackVO> getDetail(Long unpackId) {
        SpdPackageUnpackEntity entity = spdPackageUnpackDao.selectById(unpackId);
        if (entity == null) {
            return ResponseDTO.userErrorParam("拆包单不存在");
        }
        return ResponseDTO.ok(SmartBeanUtil.copy(entity, SpdPackageUnpackVO.class));
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> add(SpdPackageUnpackAddForm addForm, Long userId, String userName) {
        String unpackNo = "UP" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        
        SpdPackageUnpackEntity entity = SmartBeanUtil.copy(addForm, SpdPackageUnpackEntity.class);
        entity.setUnpackNo(unpackNo);
        entity.setStatus(1);
        entity.setCreateUserId(userId);
        entity.setCreateUserName(userName);
        entity.setCreateTime(LocalDateTime.now());
        spdPackageUnpackDao.insert(entity);
        
        // TODO: 标签作废 + 库存回滚
        return ResponseDTO.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> delete(Long unpackId) {
        spdPackageUnpackDao.deleteById(unpackId);
        return ResponseDTO.ok();
    }
}
