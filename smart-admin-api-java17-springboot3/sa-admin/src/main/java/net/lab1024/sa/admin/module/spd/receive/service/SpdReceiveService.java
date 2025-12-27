package net.lab1024.sa.admin.module.spd.receive.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.spd.receive.dao.SpdReceiveDao;
import net.lab1024.sa.admin.module.spd.receive.dao.SpdReceiveDetailDao;
import net.lab1024.sa.admin.module.spd.receive.domain.entity.SpdReceiveDetailEntity;
import net.lab1024.sa.admin.module.spd.receive.domain.entity.SpdReceiveMainEntity;
import net.lab1024.sa.admin.module.spd.receive.domain.form.SpdReceiveForm;
import net.lab1024.sa.admin.module.spd.receive.domain.form.SpdReceiveQueryForm;
import net.lab1024.sa.admin.module.spd.receive.domain.vo.SpdReceiveVO;
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
public class SpdReceiveService {

    @Autowired
    private SpdReceiveDao spdReceiveDao;
    
    @Autowired
    private SpdReceiveDetailDao spdReceiveDetailDao;

    public PageResult<SpdReceiveVO> queryPage(SpdReceiveQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        var list = spdReceiveDao.queryPage(page, queryForm, 0);
        return SmartPageUtil.convert2PageResult(page, list);
    }

    public ResponseDTO<SpdReceiveVO> getDetail(String receiveCode) {
        SpdReceiveVO vo = spdReceiveDao.getDetailByCode(receiveCode, 0);
        if (vo == null) return ResponseDTO.userErrorParam("收货单不存在");
        vo.setDetailList(spdReceiveDetailDao.queryByReceiveCode(receiveCode, 0));
        return ResponseDTO.ok(vo);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> add(SpdReceiveForm form, String loginUserId, String tenantId) {
        String receiveId = generateReceiveId();
        String receiveCode = "RC" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        
        SpdReceiveMainEntity entity = SmartBeanUtil.copy(form, SpdReceiveMainEntity.class);
        entity.setReceiveId(receiveId);
        entity.setReceiveCode(receiveCode);
        entity.setReceiveStatus(0);
        entity.setCreateUser(loginUserId);
        entity.setCreateTime(LocalDateTime.now());
        entity.setTenantId(tenantId);
        entity.setDelFlag(0);
        spdReceiveDao.insert(entity);

        form.getDetailList().forEach(detail -> {
            SpdReceiveDetailEntity detailEntity = SmartBeanUtil.copy(detail, SpdReceiveDetailEntity.class);
            detailEntity.setReceiveCode(receiveCode);
            detailEntity.setTenantId(tenantId);
            detailEntity.setDelFlag(0);
            detailEntity.setCreateBy(loginUserId);
            detailEntity.setCreateTime(LocalDateTime.now());
            spdReceiveDetailDao.insert(detailEntity);
        });

        log.info("新增收货单成功，receiveCode={}", receiveCode);
        return ResponseDTO.ok("新增成功");
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> update(SpdReceiveForm form, String loginUserId) {
        SpdReceiveMainEntity entity = spdReceiveDao.selectOne(
            new LambdaQueryWrapper<SpdReceiveMainEntity>()
                .eq(SpdReceiveMainEntity::getReceiveCode, form.getId())
                .eq(SpdReceiveMainEntity::getDelFlag, 0));
        if (entity == null) return ResponseDTO.userErrorParam("收货单不存在");
        if (entity.getReceiveStatus() != 0) return ResponseDTO.userErrorParam("只能修改待收货状态的收货单");

        SmartBeanUtil.copyProperties(form, entity);
        entity.setUpdateBy(loginUserId);
        entity.setUpdateTime(LocalDateTime.now());
        spdReceiveDao.updateById(entity);

        spdReceiveDetailDao.delete(
            new LambdaQueryWrapper<SpdReceiveDetailEntity>()
                .eq(SpdReceiveDetailEntity::getReceiveCode, entity.getReceiveCode()));

        form.getDetailList().forEach(detail -> {
            SpdReceiveDetailEntity detailEntity = SmartBeanUtil.copy(detail, SpdReceiveDetailEntity.class);
            detailEntity.setReceiveCode(entity.getReceiveCode());
            detailEntity.setDelFlag(0);
            detailEntity.setCreateBy(loginUserId);
            detailEntity.setCreateTime(LocalDateTime.now());
            spdReceiveDetailDao.insert(detailEntity);
        });

        log.info("更新收货单成功，receiveCode={}", entity.getReceiveCode());
        return ResponseDTO.ok("更新成功");
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> delete(String receiveCode, String loginUserId) {
        SpdReceiveMainEntity entity = spdReceiveDao.selectOne(
            new LambdaQueryWrapper<SpdReceiveMainEntity>()
                .eq(SpdReceiveMainEntity::getReceiveCode, receiveCode)
                .eq(SpdReceiveMainEntity::getDelFlag, 0));
        if (entity == null) return ResponseDTO.userErrorParam("收货单不存在");

        entity.setDelFlag(1);
        entity.setUpdateBy(loginUserId);
        entity.setUpdateTime(LocalDateTime.now());
        spdReceiveDao.updateById(entity);

        log.info("删除收货单成功，receiveCode={}", receiveCode);
        return ResponseDTO.ok("删除成功");
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> receive(String acceptanceCode, String warehouseId, String loginUserId, String tenantId) {
        String receiveId = "REC" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + String.format("%06d", 1);
        String receiveCode = "RC" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        
        SpdReceiveMainEntity entity = new SpdReceiveMainEntity();
        entity.setReceiveId(receiveId);
        entity.setReceiveCode(receiveCode);
        entity.setAcceptanceCode(acceptanceCode);
        entity.setWarehouseId(warehouseId);
        entity.setReceiveStatus(0);
        entity.setCreateUser(loginUserId);
        entity.setCreateTime(LocalDateTime.now());
        entity.setTenantId(tenantId);
        entity.setDelFlag(0);

        spdReceiveDao.insert(entity);
        log.info("收货成功，receiveCode={}", receiveCode);
        return ResponseDTO.ok("收货成功");
    }

    private String generateReceiveId() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        Long count = spdReceiveDao.selectCount(
            new LambdaQueryWrapper<SpdReceiveMainEntity>()
                .likeRight(SpdReceiveMainEntity::getReceiveId, "REC" + dateStr));
        return "REC" + dateStr + String.format("%06d", (count != null ? count.intValue() : 0) + 1);
    }
}
