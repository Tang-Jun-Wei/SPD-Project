package net.lab1024.sa.admin.module.spd.purchase.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.spd.purchase.dao.SpdPurchaseDao;
import net.lab1024.sa.admin.module.spd.purchase.dao.SpdPurchaseDetailDao;
import net.lab1024.sa.admin.module.spd.purchase.domain.form.SpdPurchaseForm;
import net.lab1024.sa.admin.module.spd.purchase.domain.form.SpdPurchaseQueryForm;
import net.lab1024.sa.admin.module.spd.purchase.domain.vo.SpdPurchaseVO;
import net.lab1024.sa.admin.module.spd.purchase.entity.SpdPurchaseEntity;
import net.lab1024.sa.admin.module.spd.purchase.entity.SpdPurchaseDetailEntity;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
public class SpdPurchaseService {
    @Autowired
    private SpdPurchaseDao spdPurchaseDao;
    @Autowired
    private SpdPurchaseDetailDao spdPurchaseDetailDao;

    public PageResult<SpdPurchaseVO> queryPage(SpdPurchaseQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<SpdPurchaseVO> list = spdPurchaseDao.queryPage(page, queryForm, 0);
        return SmartPageUtil.convert2PageResult(page, list);
    }

    public ResponseDTO<SpdPurchaseVO> getDetail(Long id) {
        SpdPurchaseVO vo = spdPurchaseDao.getDetailById(id, 0);
        if (vo == null) return ResponseDTO.userErrorParam("采购单不存在");
        vo.setDetailList(spdPurchaseDetailDao.queryByPurchaseId(vo.getPurchaseId(), 0));
        return ResponseDTO.ok(vo);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> add(SpdPurchaseForm form, String loginUserId, String tenantId) {
        String purchaseId = generatePurchaseId();
        String purchaseNo = "PUR" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        
        BigDecimal totalAmount = form.getDetailList().stream()
            .map(d -> d.getQuantity().multiply(d.getUnitPrice()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        SpdPurchaseEntity entity = SmartBeanUtil.copy(form, SpdPurchaseEntity.class);
        entity.setPurchaseId(purchaseId);
        entity.setPurchaseNo(purchaseNo);
        entity.setTotalAmount(totalAmount);
        entity.setPurchaseStatus(1);
        entity.setTenantId(tenantId);
        entity.setDelFlag(0);
        entity.setCreateBy(loginUserId);
        entity.setCreateTime(LocalDateTime.now());
        spdPurchaseDao.insert(entity);

        form.getDetailList().forEach(detail -> {
            SpdPurchaseDetailEntity detailEntity = SmartBeanUtil.copy(detail, SpdPurchaseDetailEntity.class);
            detailEntity.setPurchaseId(purchaseId);
            detailEntity.setTotalPrice(detail.getQuantity().multiply(detail.getUnitPrice()));
            detailEntity.setDelFlag(0);
            detailEntity.setCreateBy(loginUserId);
            detailEntity.setCreateTime(LocalDateTime.now());
            spdPurchaseDetailDao.insert(detailEntity);
        });

        log.info("新增采购单成功，purchaseId={}", purchaseId);
        return ResponseDTO.ok("新增成功");
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> update(SpdPurchaseForm form, String loginUserId) {
        SpdPurchaseEntity entity = spdPurchaseDao.selectById(form.getId());
        if (entity == null || entity.getDelFlag() == 1) return ResponseDTO.userErrorParam("采购单不存在");
        if (entity.getPurchaseStatus() != 1) return ResponseDTO.userErrorParam("只能修改待审核状态的采购单");

        BigDecimal totalAmount = form.getDetailList().stream()
            .map(d -> d.getQuantity().multiply(d.getUnitPrice()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        SmartBeanUtil.copyProperties(form, entity);
        entity.setTotalAmount(totalAmount);
        entity.setUpdateBy(loginUserId);
        entity.setUpdateTime(LocalDateTime.now());
        spdPurchaseDao.updateById(entity);

        spdPurchaseDetailDao.delete(
            new LambdaQueryWrapper<SpdPurchaseDetailEntity>()
                .eq(SpdPurchaseDetailEntity::getPurchaseId, entity.getPurchaseId())
        );

        form.getDetailList().forEach(detail -> {
            SpdPurchaseDetailEntity detailEntity = SmartBeanUtil.copy(detail, SpdPurchaseDetailEntity.class);
            detailEntity.setPurchaseId(entity.getPurchaseId());
            detailEntity.setTotalPrice(detail.getQuantity().multiply(detail.getUnitPrice()));
            detailEntity.setDelFlag(0);
            detailEntity.setCreateBy(loginUserId);
            detailEntity.setCreateTime(LocalDateTime.now());
            spdPurchaseDetailDao.insert(detailEntity);
        });

        log.info("更新采购单成功，purchaseId={}", entity.getPurchaseId());
        return ResponseDTO.ok("更新成功");
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> approve(Long id, Integer approveStatus, String approveRemark, String loginUserId) {
        SpdPurchaseEntity entity = spdPurchaseDao.selectById(id);
        if (entity == null || entity.getDelFlag() == 1) return ResponseDTO.userErrorParam("采购单不存在");
        if (entity.getPurchaseStatus() != 1) return ResponseDTO.userErrorParam("该采购单不是待审核状态");

        entity.setPurchaseStatus(approveStatus);
        entity.setUpdateBy(loginUserId);
        entity.setUpdateTime(LocalDateTime.now());
        spdPurchaseDao.updateById(entity);

        log.info("审核采购单成功，purchaseId={}，审核结果={}", entity.getPurchaseId(), approveStatus);
        return ResponseDTO.ok("审核成功");
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> delete(Long id, String loginUserId) {
        SpdPurchaseEntity entity = spdPurchaseDao.selectById(id);
        if (entity == null || entity.getDelFlag() == 1) return ResponseDTO.userErrorParam("采购单不存在");
        entity.setDelFlag(1);
        entity.setUpdateBy(loginUserId);
        entity.setUpdateTime(LocalDateTime.now());
        spdPurchaseDao.updateById(entity);
        log.info("删除采购单成功，id={}", id);
        return ResponseDTO.ok("删除成功");
    }

    private String generatePurchaseId() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        Long count = spdPurchaseDao.selectCount(new LambdaQueryWrapper<SpdPurchaseEntity>().likeRight(SpdPurchaseEntity::getPurchaseId, "PUR" + dateStr));
        return "PUR" + dateStr + String.format("%06d", (count != null ? count.intValue() : 0) + 1);
    }
}
