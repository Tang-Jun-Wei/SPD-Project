package net.lab1024.sa.admin.module.spd.apply.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.spd.apply.dao.SpdApplyDao;
import net.lab1024.sa.admin.module.spd.apply.dao.SpdApplyDetailDao;
import net.lab1024.sa.admin.module.spd.apply.domain.form.SpdApplyForm;
import net.lab1024.sa.admin.module.spd.apply.domain.form.SpdApplyQueryForm;
import net.lab1024.sa.admin.module.spd.apply.domain.vo.SpdApplyVO;
import net.lab1024.sa.admin.module.spd.apply.entity.SpdApplyEntity;
import net.lab1024.sa.admin.module.spd.apply.entity.SpdApplyDetailEntity;
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
public class SpdApplyService {

    @Autowired
    private SpdApplyDao spdApplyDao;

    @Autowired
    private SpdApplyDetailDao spdApplyDetailDao;

    public PageResult<SpdApplyVO> queryPage(SpdApplyQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<SpdApplyVO> list = spdApplyDao.queryPage(page, queryForm, 0);
        return SmartPageUtil.convert2PageResult(page, list);
    }

    public ResponseDTO<SpdApplyVO> getDetail(Long id) {
        SpdApplyVO applyVO = spdApplyDao.getDetailById(id, 0);
        if (applyVO == null) {
            return ResponseDTO.userErrorParam("申领单不存在");
        }
        applyVO.setDetailList(spdApplyDetailDao.queryByApplyId(applyVO.getApplyId(), 0));
        return ResponseDTO.ok(applyVO);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> add(SpdApplyForm form, String loginUserId, String tenantId) {
        String applyId = generateApplyId();
        String applyNo = "APL" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        SpdApplyEntity entity = SmartBeanUtil.copy(form, SpdApplyEntity.class);
        entity.setApplyId(applyId);
        entity.setApplyNo(applyNo);
        entity.setApplyUserId(loginUserId);
        entity.setApplyTime(LocalDateTime.now());
        entity.setApplyStatus(1);
        entity.setTenantId(tenantId);
        entity.setDelFlag(0);
        entity.setCreateBy(loginUserId);
        entity.setCreateTime(LocalDateTime.now());

        spdApplyDao.insert(entity);

        form.getDetailList().forEach(detail -> {
            SpdApplyDetailEntity detailEntity = SmartBeanUtil.copy(detail, SpdApplyDetailEntity.class);
            detailEntity.setApplyId(applyId);
            detailEntity.setDelFlag(0);
            detailEntity.setCreateBy(loginUserId);
            detailEntity.setCreateTime(LocalDateTime.now());
            spdApplyDetailDao.insert(detailEntity);
        });

        log.info("新增申领单成功，applyId={}", applyId);
        return ResponseDTO.ok("新增成功");
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> delete(Long id, String loginUserId) {
        SpdApplyEntity entity = spdApplyDao.selectById(id);
        if (entity == null || entity.getDelFlag() == 1) {
            return ResponseDTO.userErrorParam("申领单不存在");
        }

        entity.setDelFlag(1);
        entity.setUpdateBy(loginUserId);
        entity.setUpdateTime(LocalDateTime.now());
        spdApplyDao.updateById(entity);

        log.info("删除申领单成功，id={}", id);
        return ResponseDTO.ok("删除成功");
    }

    private String generateApplyId() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        Long count = spdApplyDao.selectCount(
            new LambdaQueryWrapper<SpdApplyEntity>()
                .likeRight(SpdApplyEntity::getApplyId, "APPLY" + dateStr)
        );
        int newSeq = (count != null ? count.intValue() : 0) + 1;
        return "APPLY" + dateStr + String.format("%06d", newSeq);
    }
}
