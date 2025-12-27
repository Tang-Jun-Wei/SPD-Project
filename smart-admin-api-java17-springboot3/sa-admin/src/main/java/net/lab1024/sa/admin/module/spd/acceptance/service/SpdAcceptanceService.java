package net.lab1024.sa.admin.module.spd.acceptance.service;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.spd.acceptance.dao.SpdAcceptanceDao;
import net.lab1024.sa.admin.module.spd.acceptance.domain.entity.SpdAcceptanceMainEntity;
import net.lab1024.sa.admin.module.spd.acceptance.domain.form.SpdAcceptanceForm;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class SpdAcceptanceService {

    @Autowired
    private SpdAcceptanceDao spdAcceptanceDao;

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> add(SpdAcceptanceForm form, String loginUserId, String tenantId) {
        String acceptanceId = "ACC" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + String.format("%06d", 1);
        String acceptanceCode = "AC" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        
        SpdAcceptanceMainEntity entity = SmartBeanUtil.copy(form, SpdAcceptanceMainEntity.class);
        entity.setAcceptanceId(acceptanceId);
        entity.setAcceptanceCode(acceptanceCode);
        entity.setAcceptanceStatus(0);
        entity.setCreateUser(loginUserId);
        entity.setCreateTime(LocalDateTime.now());
        entity.setTenantId(tenantId);
        entity.setDelFlag(0);

        spdAcceptanceDao.insert(entity);
        log.info("新增验收单成功，acceptanceCode={}", acceptanceCode);
        return ResponseDTO.ok("新增成功");
    }
}
