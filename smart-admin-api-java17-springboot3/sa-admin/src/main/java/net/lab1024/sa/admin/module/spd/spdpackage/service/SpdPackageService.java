package net.lab1024.sa.admin.module.spd.spdpackage.service;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.spd.spdpackage.dao.SpdPackageDao;
import net.lab1024.sa.admin.module.spd.spdpackage.domain.entity.SpdPackageMainEntity;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class SpdPackageService {

    @Autowired
    private SpdPackageDao spdPackageDao;

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> createPackage(String warehouseId, String loginUserId, String tenantId) {
        String packageId = "PKG" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + String.format("%06d", 1);
        String packageCode = "PK" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        
        SpdPackageMainEntity entity = new SpdPackageMainEntity();
        entity.setPackageId(packageId);
        entity.setPackageCode(packageCode);
        entity.setWarehouseId(warehouseId);
        entity.setPackageStatus(0);
        entity.setCreateUser(loginUserId);
        entity.setCreateTime(LocalDateTime.now());
        entity.setTenantId(tenantId);
        entity.setDelFlag(0);

        spdPackageDao.insert(entity);
        log.info("创建打包单成功，packageCode={}", packageCode);
        return ResponseDTO.ok("创建打包单成功");
    }
}
