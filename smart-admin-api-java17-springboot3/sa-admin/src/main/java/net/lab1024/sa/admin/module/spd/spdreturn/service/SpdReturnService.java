package net.lab1024.sa.admin.module.spd.spdreturn.service;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.spd.spdreturn.dao.SpdReturnDao;
import net.lab1024.sa.admin.module.spd.spdreturn.domain.entity.SpdReturnMainEntity;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class SpdReturnService {

    @Autowired
    private SpdReturnDao spdReturnDao;

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> returnMaterial(String warehouseId, String materialId, String batchId, Integer returnNum, String loginUserId, String tenantId) {
        String returnId = "RET" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + String.format("%06d", 1);
        String returnCode = "RT" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        
        SpdReturnMainEntity entity = new SpdReturnMainEntity();
        entity.setReturnId(returnId);
        entity.setReturnCode(returnCode);
        entity.setWarehouseId(warehouseId);
        entity.setMaterialId(materialId);
        entity.setBatchId(batchId);
        entity.setReturnNum(returnNum);
        entity.setReturnStatus(0);
        entity.setCreateUser(loginUserId);
        entity.setCreateTime(LocalDateTime.now());
        entity.setTenantId(tenantId);
        entity.setDelFlag(0);

        spdReturnDao.insert(entity);
        log.info("退库成功，returnCode={}", returnCode);
        return ResponseDTO.ok("退库成功");
    }
}
