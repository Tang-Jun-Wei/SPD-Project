package net.lab1024.sa.admin.module.spd.label.service;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.spd.label.dao.SpdLabelDao;
import net.lab1024.sa.admin.module.spd.label.domain.entity.SpdLabelEntity;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class SpdLabelService {

    @Autowired
    private SpdLabelDao spdLabelDao;

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> generateLabel(String materialId, String batchId, String warehouseId, String loginUserId, String tenantId) {
        String labelId = "LBL" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + String.format("%06d", 1);
        String labelCode = "LB" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        
        SpdLabelEntity entity = new SpdLabelEntity();
        entity.setLabelId(labelId);
        entity.setLabelCode(labelCode);
        entity.setMaterialId(materialId);
        entity.setBatchId(batchId);
        entity.setWarehouseId(warehouseId);
        entity.setLabelStatus(0);
        entity.setTenantId(tenantId);
        entity.setDelFlag(0);
        entity.setCreateBy(loginUserId);
        entity.setCreateTime(LocalDateTime.now());

        spdLabelDao.insert(entity);
        log.info("生成标签成功，labelCode={}", labelCode);
        return ResponseDTO.ok("生成标签成功");
    }
}
