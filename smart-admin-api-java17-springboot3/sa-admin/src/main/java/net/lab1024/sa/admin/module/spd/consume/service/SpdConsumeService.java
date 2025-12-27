package net.lab1024.sa.admin.module.spd.consume.service;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.spd.consume.dao.SpdConsumeDao;
import net.lab1024.sa.admin.module.spd.consume.domain.entity.SpdConsumeMainEntity;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class SpdConsumeService {

    @Autowired
    private SpdConsumeDao spdConsumeDao;

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> consume(String deptId, String materialId, String batchId, Integer consumeNum, String loginUserId, String tenantId) {
        String consumeId = "CON" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + String.format("%06d", 1);
        String consumeCode = "CS" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        
        SpdConsumeMainEntity entity = new SpdConsumeMainEntity();
        entity.setConsumeId(consumeId);
        entity.setConsumeCode(consumeCode);
        entity.setDeptId(deptId);
        entity.setMaterialId(materialId);
        entity.setBatchId(batchId);
        entity.setConsumeNum(consumeNum);
        entity.setConsumeStatus(0);
        entity.setCreateUser(loginUserId);
        entity.setCreateTime(LocalDateTime.now());
        entity.setTenantId(tenantId);
        entity.setDelFlag(0);

        spdConsumeDao.insert(entity);
        log.info("消耗记录成功，consumeCode={}", consumeCode);
        return ResponseDTO.ok("消耗记录成功");
    }
}
