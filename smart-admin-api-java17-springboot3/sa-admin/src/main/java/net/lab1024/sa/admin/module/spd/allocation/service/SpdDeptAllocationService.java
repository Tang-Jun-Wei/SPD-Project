package net.lab1024.sa.admin.module.spd.allocation.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.spd.allocation.dao.SpdDeptAllocationDao;
import net.lab1024.sa.admin.module.spd.allocation.dao.SpdDeptAllocationDetailDao;
import net.lab1024.sa.admin.module.spd.allocation.domain.entity.SpdDeptAllocationDetailEntity;
import net.lab1024.sa.admin.module.spd.allocation.domain.entity.SpdDeptAllocationEntity;
import net.lab1024.sa.admin.module.spd.allocation.domain.form.SpdDeptAllocationForm;
import net.lab1024.sa.admin.module.spd.allocation.domain.vo.SpdDeptAllocationVO;
import net.lab1024.sa.base.common.domain.PageParam;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
public class SpdDeptAllocationService {

    @Resource
    private SpdDeptAllocationDao spdDeptAllocationDao;
    
    @Resource
    private SpdDeptAllocationDetailDao spdDeptAllocationDetailDao;

    /**
     * 分页查询调拨单列表
     */
    @SuppressWarnings("unchecked")
    public ResponseDTO<PageResult<SpdDeptAllocationVO>> queryPage(Object form) {
        Page page = SmartPageUtil.convert2PageQuery((PageParam) form);
        List<SpdDeptAllocationVO> list = spdDeptAllocationDao.queryPage(page, form).getRecords();
        PageResult<SpdDeptAllocationVO> result = SmartPageUtil.convert2PageResult(page, list);
        return ResponseDTO.ok(result);
    }

    /**
     * 查询调拨单详情
     */
    public ResponseDTO<SpdDeptAllocationVO> getDetail(String allocationId) {
        SpdDeptAllocationVO detail = spdDeptAllocationDao.getDetail(allocationId);
        return ResponseDTO.ok(detail);
    }

    /**
     * 新增调拨单（主表+明细表事务）
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> add(SpdDeptAllocationForm form, String loginUserId, String tenantId) {
        String allocationId = generateAllocationId();
        String allocationCode = "DB-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-" + String.format("%03d", System.currentTimeMillis() % 1000);
        
        SpdDeptAllocationEntity entity = new SpdDeptAllocationEntity();
        entity.setAllocationId(allocationId);
        entity.setAllocationCode(allocationCode);
        entity.setFromNodeId(form.getFromNodeId());
        entity.setToNodeId(form.getToNodeId());
        entity.setAllocationStatus(1);
        entity.setApplyUserId(loginUserId);
        entity.setApplyTime(LocalDateTime.now());
        entity.setRemark(form.getRemark());
        entity.setTenantId(tenantId);
        entity.setCreateBy(loginUserId);
        entity.setCreateTime(LocalDateTime.now());
        
        spdDeptAllocationDao.insert(entity);
        
        if (form.getDetailList() != null && !form.getDetailList().isEmpty()) {
            for (SpdDeptAllocationForm.DetailItem item : form.getDetailList()) {
                SpdDeptAllocationDetailEntity detail = new SpdDeptAllocationDetailEntity();
                detail.setAllocationId(allocationId);
                detail.setMaterialId(item.getMaterialId());
                detail.setBatchId(item.getBatchId());
                detail.setAllocationQuantity(item.getAllocationQuantity());
                detail.setRemark(item.getRemark());
                detail.setTenantId(tenantId);
                detail.setCreateTime(LocalDateTime.now());
                spdDeptAllocationDetailDao.insert(detail);
            }
        }
        
        return ResponseDTO.ok("新增成功");
    }

    /**
     * 审核调拨单（通过/驳回）
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> audit(String allocationId, Integer auditStatus, String auditOpinion, String loginUserId) {
        SpdDeptAllocationEntity entity = spdDeptAllocationDao.selectOne(
                new LambdaQueryWrapper<SpdDeptAllocationEntity>()
                        .eq(SpdDeptAllocationEntity::getAllocationId, allocationId)
        );
        
        if (entity == null) {
            return ResponseDTO.userErrorParam("调拨单不存在");
        }
        
        if (entity.getAllocationStatus() != 1) {
            return ResponseDTO.userErrorParam("该调拨单已审核，无法重复审核");
        }
        
        entity.setAllocationStatus(auditStatus);
        entity.setAuditUserId(loginUserId);
        entity.setAuditTime(LocalDateTime.now());
        entity.setAuditOpinion(auditOpinion);
        entity.setUpdateBy(loginUserId);
        entity.setUpdateTime(LocalDateTime.now());
        
        spdDeptAllocationDao.updateById(entity);
        
        return ResponseDTO.ok("审核成功");
    }

    /**
     * 删除调拨单（仅待审核状态可删除，级联删除明细）
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> delete(String allocationId, String loginUserId) {
        SpdDeptAllocationEntity entity = spdDeptAllocationDao.selectOne(
                new LambdaQueryWrapper<SpdDeptAllocationEntity>()
                        .eq(SpdDeptAllocationEntity::getAllocationId, allocationId)
        );
        
        if (entity == null) {
            return ResponseDTO.userErrorParam("调拨单不存在");
        }
        
        if (entity.getAllocationStatus() != 1) {
            return ResponseDTO.userErrorParam("只有待审核状态才能删除");
        }
        
        entity.setDelFlag(1);
        entity.setUpdateBy(loginUserId);
        entity.setUpdateTime(LocalDateTime.now());
        spdDeptAllocationDao.updateById(entity);
        
        List<SpdDeptAllocationDetailEntity> details = spdDeptAllocationDetailDao.selectList(
                new LambdaQueryWrapper<SpdDeptAllocationDetailEntity>()
                        .eq(SpdDeptAllocationDetailEntity::getAllocationId, allocationId)
        );
        
        for (SpdDeptAllocationDetailEntity detail : details) {
            detail.setDelFlag(1);
            spdDeptAllocationDetailDao.updateById(detail);
        }
        
        return ResponseDTO.ok("删除成功");
    }

    /**
     * 生成调拨单业务ID
     */
    private String generateAllocationId() {
        String prefix = "ALLOC";
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        Long count = spdDeptAllocationDao.selectCount(
                new LambdaQueryWrapper<SpdDeptAllocationEntity>()
                        .likeRight(SpdDeptAllocationEntity::getAllocationId, prefix + dateStr)
        );
        int newSeq = (count != null ? count.intValue() : 0) + 1;
        return prefix + dateStr + String.format("%06d", newSeq);
    }
}
