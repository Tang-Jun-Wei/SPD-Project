package net.lab1024.sa.admin.module.spd.spdpackage.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.spd.spdpackage.dao.SpdPackageDao;
import net.lab1024.sa.admin.module.spd.spdpackage.domain.entity.SpdPackageMainEntity;
import net.lab1024.sa.admin.module.spd.spdpackage.domain.form.SpdPackageForm;
import net.lab1024.sa.admin.module.spd.spdpackage.domain.form.SpdPackageQueryForm;
import net.lab1024.sa.admin.module.spd.spdpackage.domain.vo.SpdPackageVO;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
public class SpdPackageService {

    @Autowired
    private SpdPackageDao spdPackageDao;

    public PageResult<SpdPackageVO> queryPage(SpdPackageQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<SpdPackageVO> list = spdPackageDao.queryPage(page, queryForm, 0);
        return SmartPageUtil.convert2PageResult(page, list);
    }

    public ResponseDTO<SpdPackageVO> getDetail(String packageId) {
        SpdPackageVO vo = spdPackageDao.getDetail(packageId, 0);
        if (vo == null) {
            return ResponseDTO.userErrorParam("打包单不存在");
        }
        return ResponseDTO.ok(vo);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> add(SpdPackageForm form, String loginUserId, String tenantId) {
        String packageId = generatePackageId();
        String packageCode = "PKG" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        
        SpdPackageMainEntity entity = SmartBeanUtil.copy(form, SpdPackageMainEntity.class);
        entity.setPackageId(packageId);
        entity.setPackageCode(packageCode);
        entity.setPackageStatus(0);
        entity.setCreateUser(loginUserId);
        entity.setCreateTime(LocalDateTime.now());
        entity.setTenantId(tenantId);
        entity.setDelFlag(0);

        spdPackageDao.insert(entity);
        log.info("新增打包单成功，packageId={}", packageId);
        return ResponseDTO.ok("新增成功");
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> update(SpdPackageForm form, String loginUserId) {
        if (form.getPackageId() == null) {
            return ResponseDTO.userErrorParam("打包单ID不能为空");
        }

        SpdPackageMainEntity entity = spdPackageDao.selectOne(
                new LambdaQueryWrapper<SpdPackageMainEntity>()
                        .eq(SpdPackageMainEntity::getPackageId, form.getPackageId())
                        .eq(SpdPackageMainEntity::getDelFlag, 0));
        if (entity == null) {
            return ResponseDTO.userErrorParam("打包单不存在");
        }

        SmartBeanUtil.copyProperties(form, entity);
        entity.setUpdateBy(loginUserId);
        entity.setUpdateTime(LocalDateTime.now());
        spdPackageDao.updateById(entity);

        log.info("更新打包单成功，packageId={}", form.getPackageId());
        return ResponseDTO.ok("更新成功");
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> delete(String packageId, String loginUserId) {
        SpdPackageMainEntity entity = spdPackageDao.selectOne(
                new LambdaQueryWrapper<SpdPackageMainEntity>()
                        .eq(SpdPackageMainEntity::getPackageId, packageId)
                        .eq(SpdPackageMainEntity::getDelFlag, 0));
        if (entity == null) {
            return ResponseDTO.userErrorParam("打包单不存在");
        }

        entity.setDelFlag(1);
        entity.setUpdateBy(loginUserId);
        entity.setUpdateTime(LocalDateTime.now());
        spdPackageDao.updateById(entity);

        log.info("删除打包单成功，packageId={}", packageId);
        return ResponseDTO.ok("删除成功");
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> updateStatus(String packageId, Integer packageStatus, String loginUserId) {
        SpdPackageMainEntity entity = spdPackageDao.selectOne(
                new LambdaQueryWrapper<SpdPackageMainEntity>()
                        .eq(SpdPackageMainEntity::getPackageId, packageId)
                        .eq(SpdPackageMainEntity::getDelFlag, 0));
        if (entity == null) {
            return ResponseDTO.userErrorParam("打包单不存在");
        }

        entity.setPackageStatus(packageStatus);
        entity.setUpdateBy(loginUserId);
        entity.setUpdateTime(LocalDateTime.now());
        spdPackageDao.updateById(entity);

        log.info("更新打包单状态成功，packageId={}，status={}", packageId, packageStatus);
        return ResponseDTO.ok("状态更新成功");
    }

    private String generatePackageId() {
        String prefix = "PKG";
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        Long count = spdPackageDao.selectCount(
                new LambdaQueryWrapper<SpdPackageMainEntity>()
                        .likeRight(SpdPackageMainEntity::getPackageId, prefix + dateStr));
        int newSeq = (count != null ? count.intValue() : 0) + 1;
        return prefix + dateStr + String.format("%06d", newSeq);
    }
}
