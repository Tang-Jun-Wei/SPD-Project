package net.lab1024.sa.admin.module.spd.supplierreturn.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import net.lab1024.sa.admin.module.spd.supplierreturn.dao.SpdSupplierReturnDao;
import net.lab1024.sa.admin.module.spd.supplierreturn.dao.SpdSupplierReturnDetailDao;
import net.lab1024.sa.admin.module.spd.supplierreturn.domain.entity.SpdSupplierReturnDetailEntity;
import net.lab1024.sa.admin.module.spd.supplierreturn.domain.entity.SpdSupplierReturnEntity;
import net.lab1024.sa.admin.module.spd.supplierreturn.domain.form.SpdSupplierReturnForm;
import net.lab1024.sa.admin.module.spd.supplierreturn.domain.vo.SpdSupplierReturnVO;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class SpdSupplierReturnService {

    @Resource
    private SpdSupplierReturnDao spdSupplierReturnDao;

    @Resource
    private SpdSupplierReturnDetailDao spdSupplierReturnDetailDao;

    /**
     * 分页查询退供应商单列表
     */
    public ResponseDTO<PageResult<SpdSupplierReturnVO>> queryPage(Object form) {
        Page<SpdSupplierReturnVO> page = SmartPageUtil.convert2PageQuery(form);
        Page<SpdSupplierReturnVO> pageResult = spdSupplierReturnDao.queryPage(page, form);
        PageResult<SpdSupplierReturnVO> result = SmartPageUtil.convert2PageResult(pageResult);
        return ResponseDTO.ok(result);
    }

    /**
     * 查询退供应商单详情
     */
    public ResponseDTO<SpdSupplierReturnVO> getDetail(String supplierReturnId) {
        SpdSupplierReturnVO detail = spdSupplierReturnDao.getDetail(supplierReturnId);
        
        // 查询明细列表
        List<SpdSupplierReturnDetailEntity> detailList = spdSupplierReturnDetailDao.selectList(
                new LambdaQueryWrapper<SpdSupplierReturnDetailEntity>()
                        .eq(SpdSupplierReturnDetailEntity::getSupplierReturnId, supplierReturnId)
        );
        
        return ResponseDTO.ok(detail);
    }

    /**
     * 新增退供应商单（主表+明细表事务）
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> add(SpdSupplierReturnForm form, String loginUserId, String tenantId) {
        String supplierReturnId = generateSupplierReturnId();
        String supplierReturnCode = "SUPRET-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + 
                                   "-" + String.format("%03d", System.currentTimeMillis() % 1000);
        
        // 插入主表
        SpdSupplierReturnEntity entity = new SpdSupplierReturnEntity();
        entity.setSupplierReturnId(supplierReturnId);
        entity.setSupplierReturnCode(supplierReturnCode);
        entity.setWarehouseNodeId(form.getWarehouseNodeId());
        entity.setSupplierId(form.getSupplierId());
        entity.setReturnType(form.getReturnType());
        entity.setReturnStatus(1); // 待审核
        entity.setApplyUserId(loginUserId);
        entity.setApplyTime(LocalDateTime.now());
        entity.setRemark(form.getRemark());
        entity.setTenantId(tenantId);
        entity.setCreateBy(loginUserId);
        
        spdSupplierReturnDao.insert(entity);
        
        // 批量插入明细表
        if (form.getDetailList() != null && !form.getDetailList().isEmpty()) {
            for (SpdSupplierReturnForm.DetailItem item : form.getDetailList()) {
                SpdSupplierReturnDetailEntity detail = new SpdSupplierReturnDetailEntity();
                detail.setSupplierReturnId(supplierReturnId);
                detail.setMaterialId(item.getMaterialId());
                detail.setBatchId(item.getBatchId());
                detail.setReturnQuantity(item.getReturnQuantity());
                detail.setReturnReason(item.getReturnReason());
                detail.setTenantId(tenantId);
                
                spdSupplierReturnDetailDao.insert(detail);
            }
        }
        
        return ResponseDTO.ok("新增成功");
    }

    /**
     * 审核退供应商单（通过/驳回）
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> audit(String supplierReturnId, Integer auditStatus, String auditOpinion, String loginUserId) {
        SpdSupplierReturnEntity entity = spdSupplierReturnDao.selectOne(
                new LambdaQueryWrapper<SpdSupplierReturnEntity>()
                        .eq(SpdSupplierReturnEntity::getSupplierReturnId, supplierReturnId)
        );
        
        if (entity == null) {
            return ResponseDTO.userErrorParam("退供应商单不存在");
        }
        
        if (entity.getReturnStatus() != 1) {
            return ResponseDTO.userErrorParam("该退供应商单已审核，无法重复审核");
        }
        
        // 更新审核信息
        entity.setReturnStatus(auditStatus); // 2=已通过 3=已驳回
        entity.setAuditUserId(loginUserId);
        entity.setAuditTime(LocalDateTime.now());
        entity.setAuditOpinion(auditOpinion);
        entity.setUpdateBy(loginUserId);
        
        spdSupplierReturnDao.updateById(entity);
        return ResponseDTO.ok("审核成功");
    }

    /**
     * 删除退供应商单（仅待审核状态可删除，级联删除明细）
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> delete(String supplierReturnId, String loginUserId) {
        SpdSupplierReturnEntity entity = spdSupplierReturnDao.selectOne(
                new LambdaQueryWrapper<SpdSupplierReturnEntity>()
                        .eq(SpdSupplierReturnEntity::getSupplierReturnId, supplierReturnId)
        );
        
        if (entity == null) {
            return ResponseDTO.userErrorParam("退供应商单不存在");
        }
        
        if (entity.getReturnStatus() != 1) {
            return ResponseDTO.userErrorParam("只能删除待审核状态的退供应商单");
        }
        
        // 逻辑删除主表
        entity.setDelFlag(1);
        entity.setUpdateBy(loginUserId);
        spdSupplierReturnDao.updateById(entity);
        
        // 逻辑删除明细表
        List<SpdSupplierReturnDetailEntity> detailList = spdSupplierReturnDetailDao.selectList(
                new LambdaQueryWrapper<SpdSupplierReturnDetailEntity>()
                        .eq(SpdSupplierReturnDetailEntity::getSupplierReturnId, supplierReturnId)
        );
        
        for (SpdSupplierReturnDetailEntity detail : detailList) {
            detail.setDelFlag(1);
            spdSupplierReturnDetailDao.updateById(detail);
        }
        
        return ResponseDTO.ok("删除成功");
    }

    /**
     * 生成退供应商单业务ID
     */
    private String generateSupplierReturnId() {
        String prefix = "SUPRET";
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long sequence = System.currentTimeMillis() % 1000000;
        return String.format("%s%s%06d", prefix, dateStr, sequence);
    }
}
