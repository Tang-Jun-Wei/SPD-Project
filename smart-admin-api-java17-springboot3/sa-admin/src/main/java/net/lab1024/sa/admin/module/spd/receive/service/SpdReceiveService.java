package net.lab1024.sa.admin.module.spd.receive.service;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.spd.receive.dao.SpdReceiveDao;
import net.lab1024.sa.admin.module.spd.receive.domain.entity.SpdReceiveMainEntity;
import net.lab1024.sa.base.common.domain.ResponseDTO;
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
}
