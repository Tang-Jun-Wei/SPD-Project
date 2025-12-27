package net.lab1024.sa.admin.module.spd.bulk.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.spd.bulk.dao.SpdBulkMaterialDao;
import net.lab1024.sa.admin.module.spd.bulk.entity.SpdBulkMaterialEntity;
import net.lab1024.sa.admin.module.spd.bulk.domain.form.SpdBulkMaterialForm;
import net.lab1024.sa.admin.module.spd.bulk.domain.form.SpdBulkMaterialQueryForm;
import net.lab1024.sa.admin.module.spd.bulk.domain.vo.SpdBulkMaterialVO;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class SpdBulkMaterialService {

    @Autowired
    private SpdBulkMaterialDao spdBulkMaterialDao;

    public PageResult<SpdBulkMaterialVO> queryPage(SpdBulkMaterialQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        var list = spdBulkMaterialDao.queryPage(page, queryForm, 0);
        return SmartPageUtil.convert2PageResult(page, list);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> add(SpdBulkMaterialForm form, String loginUserId, String tenantId) {
        String bulkId = generateBulkId();
        
        SpdBulkMaterialEntity entity = SmartBeanUtil.copy(form, SpdBulkMaterialEntity.class);
        entity.setBulkId(bulkId);
        entity.setUsedNum(0);
        entity.setRemainingNum(form.getTotalNum());
        entity.setCreateTime(LocalDateTime.now());
        entity.setTenantId(tenantId);
        entity.setDelFlag(0);
        spdBulkMaterialDao.insert(entity);

        log.info("新增散货成功，bulkId={}", bulkId);
        return ResponseDTO.ok("新增成功");
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> consume(String bulkId, Integer consumeNum, String loginUserId) {
        SpdBulkMaterialEntity entity = spdBulkMaterialDao.selectOne(
            new LambdaQueryWrapper<SpdBulkMaterialEntity>()
                .eq(SpdBulkMaterialEntity::getBulkId, bulkId)
                .eq(SpdBulkMaterialEntity::getDelFlag, 0));
        if (entity == null) return ResponseDTO.userErrorParam("散货不存在");
        if (entity.getRemainingNum() < consumeNum) return ResponseDTO.userErrorParam("剩余数量不足");

        entity.setUsedNum(entity.getUsedNum() + consumeNum);
        entity.setRemainingNum(entity.getTotalNum() - entity.getUsedNum());
        entity.setUpdateBy(loginUserId);
        entity.setUpdateTime(LocalDateTime.now());
        spdBulkMaterialDao.updateById(entity);

        log.info("散货消耗成功，bulkId={}，消耗数量={}", bulkId, consumeNum);
        return ResponseDTO.ok("消耗成功");
    }

    private String generateBulkId() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        Long count = spdBulkMaterialDao.selectCount(
            new LambdaQueryWrapper<SpdBulkMaterialEntity>()
                .likeRight(SpdBulkMaterialEntity::getBulkId, "BULK" + dateStr));
        return "BULK" + dateStr + String.format("%06d", (count != null ? count.intValue() : 0) + 1);
    }
}
