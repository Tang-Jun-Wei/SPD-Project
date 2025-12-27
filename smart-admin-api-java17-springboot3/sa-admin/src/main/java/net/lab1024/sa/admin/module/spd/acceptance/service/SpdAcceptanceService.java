package net.lab1024.sa.admin.module.spd.acceptance.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.spd.acceptance.dao.SpdAcceptanceDao;
import net.lab1024.sa.admin.module.spd.acceptance.dao.SpdAcceptanceDetailDao;
import net.lab1024.sa.admin.module.spd.acceptance.domain.form.SpdAcceptanceDetailForm;
import net.lab1024.sa.admin.module.spd.acceptance.domain.form.SpdAcceptanceForm;
import net.lab1024.sa.admin.module.spd.acceptance.domain.form.SpdAcceptanceQueryForm;
import net.lab1024.sa.admin.module.spd.acceptance.domain.vo.SpdAcceptanceDetailVO;
import net.lab1024.sa.admin.module.spd.acceptance.domain.vo.SpdAcceptanceVO;
import net.lab1024.sa.admin.module.spd.acceptance.entity.SpdAcceptanceDetailEntity;
import net.lab1024.sa.admin.module.spd.acceptance.entity.SpdAcceptanceEntity;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
public class SpdAcceptanceService {

    @Autowired
    private SpdAcceptanceDao spdAcceptanceDao;

    @Autowired
    private SpdAcceptanceDetailDao spdAcceptanceDetailDao;

    /**
     * 分页查询
     */
    public PageResult<SpdAcceptanceVO> queryPage(SpdAcceptanceQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<SpdAcceptanceVO> list = spdAcceptanceDao.queryPage(page, queryForm, 0);
        return SmartPageUtil.convert2PageResult(page, list);
    }

    /**
     * 查询详情
     */
    public ResponseDTO<SpdAcceptanceVO> queryDetail(String acceptanceId) {
        SpdAcceptanceVO vo = spdAcceptanceDao.queryDetail(acceptanceId, 0);
        if (vo == null) {
            return ResponseDTO.userErrorParam("验收单不存在");
        }
        List<SpdAcceptanceDetailVO> detailList = spdAcceptanceDetailDao.queryByAcceptanceId(acceptanceId, 0);
        vo.setDetailList(detailList);
        return ResponseDTO.ok(vo);
    }

    /**
     * 新增
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> add(SpdAcceptanceForm form, String loginUserId, String tenantId) {
        String acceptanceId = generateAcceptanceId();
        String acceptanceNo = "ACC" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        BigDecimal totalAmount = form.getDetailList().stream()
                .map(d -> d.getAcceptanceQuantity().multiply(d.getUnitPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        SpdAcceptanceEntity entity = SmartBeanUtil.copy(form, SpdAcceptanceEntity.class);
        entity.setAcceptanceId(acceptanceId);
        entity.setAcceptanceNo(acceptanceNo);
        entity.setTotalAmount(totalAmount);
        entity.setAcceptanceStatus(1);
        entity.setTenantId(tenantId);
        entity.setDelFlag(0);
        entity.setCreateBy(loginUserId);
        entity.setCreateTime(LocalDateTime.now());
        spdAcceptanceDao.insert(entity);

        form.getDetailList().forEach(detail -> {
            SpdAcceptanceDetailEntity detailEntity = SmartBeanUtil.copy(detail, SpdAcceptanceDetailEntity.class);
            detailEntity.setAcceptanceId(acceptanceId);
            detailEntity.setTotalPrice(detail.getAcceptanceQuantity().multiply(detail.getUnitPrice()));
            detailEntity.setDelFlag(0);
            detailEntity.setCreateBy(loginUserId);
            detailEntity.setCreateTime(LocalDateTime.now());
            spdAcceptanceDetailDao.insert(detailEntity);
        });

        log.info("新增验收单成功，acceptanceId={}", acceptanceId);
        return ResponseDTO.ok("新增成功");
    }

    /**
     * 更新
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> update(SpdAcceptanceForm form, String loginUserId) {
        SpdAcceptanceEntity entity = spdAcceptanceDao.selectOne(
                new LambdaQueryWrapper<SpdAcceptanceEntity>()
                        .eq(SpdAcceptanceEntity::getAcceptanceId, form.getAcceptanceId())
                        .eq(SpdAcceptanceEntity::getDelFlag, 0));
        if (entity == null) {
            return ResponseDTO.userErrorParam("验收单不存在");
        }

        BigDecimal totalAmount = form.getDetailList().stream()
                .map(d -> d.getAcceptanceQuantity().multiply(d.getUnitPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        SmartBeanUtil.copyProperties(form, entity);
        entity.setTotalAmount(totalAmount);
        entity.setUpdateBy(loginUserId);
        entity.setUpdateTime(LocalDateTime.now());
        spdAcceptanceDao.updateById(entity);

        spdAcceptanceDetailDao.delete(
                new LambdaQueryWrapper<SpdAcceptanceDetailEntity>()
                        .eq(SpdAcceptanceDetailEntity::getAcceptanceId, form.getAcceptanceId()));

        form.getDetailList().forEach(detail -> {
            SpdAcceptanceDetailEntity detailEntity = SmartBeanUtil.copy(detail, SpdAcceptanceDetailEntity.class);
            detailEntity.setAcceptanceId(form.getAcceptanceId());
            detailEntity.setTotalPrice(detail.getAcceptanceQuantity().multiply(detail.getUnitPrice()));
            detailEntity.setDelFlag(0);
            detailEntity.setCreateBy(loginUserId);
            detailEntity.setCreateTime(LocalDateTime.now());
            spdAcceptanceDetailDao.insert(detailEntity);
        });

        log.info("更新验收单成功，acceptanceId={}", form.getAcceptanceId());
        return ResponseDTO.ok("更新成功");
    }

    /**
     * 删除
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> delete(String acceptanceId, String loginUserId) {
        SpdAcceptanceEntity entity = spdAcceptanceDao.selectOne(
                new LambdaQueryWrapper<SpdAcceptanceEntity>()
                        .eq(SpdAcceptanceEntity::getAcceptanceId, acceptanceId)
                        .eq(SpdAcceptanceEntity::getDelFlag, 0));
        if (entity == null) {
            return ResponseDTO.userErrorParam("验收单不存在");
        }

        entity.setDelFlag(1);
        entity.setUpdateBy(loginUserId);
        entity.setUpdateTime(LocalDateTime.now());
        spdAcceptanceDao.updateById(entity);

        spdAcceptanceDetailDao.update(null,
                new LambdaUpdateWrapper<SpdAcceptanceDetailEntity>()
                        .eq(SpdAcceptanceDetailEntity::getAcceptanceId, acceptanceId)
                        .set(SpdAcceptanceDetailEntity::getDelFlag, 1)
                        .set(SpdAcceptanceDetailEntity::getUpdateBy, loginUserId)
                        .set(SpdAcceptanceDetailEntity::getUpdateTime, LocalDateTime.now()));

        log.info("删除验收单成功，acceptanceId={}", acceptanceId);
        return ResponseDTO.ok("删除成功");
    }

    private String generateAcceptanceId() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        Long count = spdAcceptanceDao.selectCount(new LambdaQueryWrapper<SpdAcceptanceEntity>()
                .likeRight(SpdAcceptanceEntity::getAcceptanceId, "ACC" + dateStr));
        return "ACC" + dateStr + String.format("%06d", (count != null ? count.intValue() : 0) + 1);
    }
}
