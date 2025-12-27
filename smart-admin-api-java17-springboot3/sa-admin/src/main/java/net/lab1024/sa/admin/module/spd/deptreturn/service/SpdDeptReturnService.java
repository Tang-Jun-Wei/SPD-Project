package net.lab1024.sa.admin.module.spd.deptreturn.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import net.lab1024.sa.admin.module.spd.deptreturn.dao.SpdDeptReturnDao;
import net.lab1024.sa.admin.module.spd.deptreturn.dao.SpdDeptReturnDetailDao;
import net.lab1024.sa.admin.module.spd.deptreturn.domain.entity.SpdDeptReturnDetailEntity;
import net.lab1024.sa.admin.module.spd.deptreturn.domain.entity.SpdDeptReturnEntity;
import net.lab1024.sa.admin.module.spd.deptreturn.domain.form.SpdDeptReturnForm;
import net.lab1024.sa.admin.module.spd.deptreturn.domain.vo.SpdDeptReturnVO;
import net.lab1024.sa.base.common.domain.PageParam;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class SpdDeptReturnService {

    @Resource
    private SpdDeptReturnDao spdDeptReturnDao;

    @Resource
    private SpdDeptReturnDetailDao spdDeptReturnDetailDao;

    /**
     * 分页查询退库单列表
     */
    @SuppressWarnings("unchecked")
    public ResponseDTO<PageResult<SpdDeptReturnVO>> queryPage(Object form) {
        Page page = SmartPageUtil.convert2PageQuery((PageParam) form);
        List<SpdDeptReturnVO> list = spdDeptReturnDao.queryPage(page, form).getRecords();
        PageResult<SpdDeptReturnVO> result = SmartPageUtil.convert2PageResult(page, list);
        return ResponseDTO.ok(result);
    }

    /**
     * 查询退库单详情
     */
    public ResponseDTO<SpdDeptReturnVO> getDetail(String returnId) {
        SpdDeptReturnVO detail = spdDeptReturnDao.getDetail(returnId);
        
        // 查询明细列表
        List<SpdDeptReturnDetailEntity> detailList = spdDeptReturnDetailDao.selectList(
                new LambdaQueryWrapper<SpdDeptReturnDetailEntity>()
                        .eq(SpdDeptReturnDetailEntity::getReturnId, returnId)
        );
        
        return ResponseDTO.ok(detail);
    }

    /**
     * 新增退库单（主表+明细表事务）
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> add(SpdDeptReturnForm form, String loginUserId, String tenantId) {
        String returnId = generateReturnId();
        String returnCode = "TK-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + 
                           "-" + String.format("%03d", System.currentTimeMillis() % 1000);
        
        // 插入主表
        SpdDeptReturnEntity entity = new SpdDeptReturnEntity();
        entity.setReturnId(returnId);
        entity.setReturnCode(returnCode);
        entity.setFromNodeId(form.getFromNodeId());
        entity.setToNodeId(form.getToNodeId());
        entity.setReturnType(form.getReturnType());
        entity.setReturnStatus(1); // 待审核
        entity.setApplyUserId(loginUserId);
        entity.setApplyTime(LocalDateTime.now());
        entity.setRemark(form.getRemark());
        entity.setTenantId(tenantId);
        entity.setCreateBy(loginUserId);
        
        spdDeptReturnDao.insert(entity);
        
        // 批量插入明细表
        if (form.getDetailList() != null && !form.getDetailList().isEmpty()) {
            for (SpdDeptReturnForm.DetailItem item : form.getDetailList()) {
                SpdDeptReturnDetailEntity detail = new SpdDeptReturnDetailEntity();
                detail.setReturnId(returnId);
                detail.setMaterialId(item.getMaterialId());
                detail.setBatchId(item.getBatchId());
                detail.setReturnQuantity(item.getReturnQuantity());
                detail.setReturnReason(item.getReturnReason());
                detail.setTenantId(tenantId);
                
                spdDeptReturnDetailDao.insert(detail);
            }
        }
        
        return ResponseDTO.ok("新增成功");
    }

    /**
     * 审核退库单（通过/驳回）
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> audit(String returnId, Integer auditStatus, String auditOpinion, String loginUserId) {
        SpdDeptReturnEntity entity = spdDeptReturnDao.selectOne(
                new LambdaQueryWrapper<SpdDeptReturnEntity>()
                        .eq(SpdDeptReturnEntity::getReturnId, returnId)
        );
        
        if (entity == null) {
            return ResponseDTO.userErrorParam("退库单不存在");
        }
        
        if (entity.getReturnStatus() != 1) {
            return ResponseDTO.userErrorParam("该退库单已审核，无法重复审核");
        }
        
        // 更新审核信息
        entity.setReturnStatus(auditStatus); // 2=已通过 3=已驳回
        entity.setAuditUserId(loginUserId);
        entity.setAuditTime(LocalDateTime.now());
        entity.setAuditOpinion(auditOpinion);
        entity.setUpdateBy(loginUserId);
        
        spdDeptReturnDao.updateById(entity);
        return ResponseDTO.ok("审核成功");
    }

    /**
     * 删除退库单（仅待审核状态可删除，级联删除明细）
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> delete(String returnId, String loginUserId) {
        SpdDeptReturnEntity entity = spdDeptReturnDao.selectOne(
                new LambdaQueryWrapper<SpdDeptReturnEntity>()
                        .eq(SpdDeptReturnEntity::getReturnId, returnId)
        );
        
        if (entity == null) {
            return ResponseDTO.userErrorParam("退库单不存在");
        }
        
        if (entity.getReturnStatus() != 1) {
            return ResponseDTO.userErrorParam("只能删除待审核状态的退库单");
        }
        
        // 逻辑删除主表
        entity.setDelFlag(1);
        entity.setUpdateBy(loginUserId);
        spdDeptReturnDao.updateById(entity);
        
        // 逻辑删除明细表
        List<SpdDeptReturnDetailEntity> detailList = spdDeptReturnDetailDao.selectList(
                new LambdaQueryWrapper<SpdDeptReturnDetailEntity>()
                        .eq(SpdDeptReturnDetailEntity::getReturnId, returnId)
        );
        
        for (SpdDeptReturnDetailEntity detail : detailList) {
            detail.setDelFlag(1);
            spdDeptReturnDetailDao.updateById(detail);
        }
        
        return ResponseDTO.ok("删除成功");
    }

    /**
     * 生成退库单业务ID
     */
    private String generateReturnId() {
        String prefix = "RETURN";
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long sequence = System.currentTimeMillis() % 1000000;
        return String.format("%s%s%06d", prefix, dateStr, sequence);
    }
}
