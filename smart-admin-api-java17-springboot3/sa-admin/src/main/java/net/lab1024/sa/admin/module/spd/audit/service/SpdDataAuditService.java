package net.lab1024.sa.admin.module.spd.audit.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.spd.audit.dao.SpdDataAuditDao;
import net.lab1024.sa.admin.module.spd.audit.domain.entity.SpdDataAuditEntity;
import net.lab1024.sa.admin.module.spd.audit.domain.form.SpdDataAuditProcessForm;
import net.lab1024.sa.admin.module.spd.audit.domain.form.SpdDataAuditQueryForm;
import net.lab1024.sa.admin.module.spd.audit.domain.form.SpdDataAuditSubmitForm;
import net.lab1024.sa.admin.module.spd.audit.domain.vo.SpdDataAuditVO;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class SpdDataAuditService {

    @Resource
    private SpdDataAuditDao spdDataAuditDao;

    /**
     * 分页查询审核记录
     */
    public ResponseDTO<PageResult<SpdDataAuditVO>> queryPage(SpdDataAuditQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        Page<SpdDataAuditVO> pageData = spdDataAuditDao.queryPage(page, queryForm, 0);
        
        // 转换类型名称和状态名称
        for (SpdDataAuditVO vo : pageData.getRecords()) {
            vo.setAuditTypeName(getAuditTypeName(vo.getAuditType()));
            vo.setActionTypeName(getActionTypeName(vo.getActionType()));
            vo.setAuditStatusName(getAuditStatusName(vo.getAuditStatus()));
        }
        
        PageResult<SpdDataAuditVO> pageResult = SmartPageUtil.convert2PageResult(page, pageData.getRecords());
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 查询审核详情
     */
    public ResponseDTO<SpdDataAuditVO> getDetail(Long id) {
        SpdDataAuditVO vo = spdDataAuditDao.getDetailById(id);
        if (vo == null) {
            return ResponseDTO.userErrorParam("审核记录不存在");
        }
        
        vo.setAuditTypeName(getAuditTypeName(vo.getAuditType()));
        vo.setActionTypeName(getActionTypeName(vo.getActionType()));
        vo.setAuditStatusName(getAuditStatusName(vo.getAuditStatus()));
        
        return ResponseDTO.ok(vo);
    }

    /**
     * 提交审核申请
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> submitAudit(SpdDataAuditSubmitForm form, String loginUserId, String tenantId) {
        // 生成审核业务ID
        String auditId = generateAuditId();
        
        // 构建审核实体
        SpdDataAuditEntity entity = SmartBeanUtil.copy(form, SpdDataAuditEntity.class);
        entity.setAuditId(auditId);
        entity.setAuditStatus(0); // 待审核
        entity.setSubmitUser(loginUserId);
        entity.setSubmitTime(LocalDateTime.now());
        entity.setTenantId(tenantId);
        entity.setDelFlag(0);
        entity.setCreateBy(loginUserId);
        entity.setCreateTime(LocalDateTime.now());
        
        spdDataAuditDao.insert(entity);
        
        log.info("提交审核申请成功，auditId={}, auditType={}, targetId={}", 
            auditId, form.getAuditType(), form.getTargetId());
        
        return ResponseDTO.ok("提交审核申请成功");
    }

    /**
     * 处理审核（通过/拒绝）
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> processAudit(SpdDataAuditProcessForm form, String loginUserId) {
        // 查询审核记录
        SpdDataAuditEntity entity = spdDataAuditDao.selectById(form.getId());
        if (entity == null || entity.getDelFlag() == 1) {
            return ResponseDTO.userErrorParam("审核记录不存在");
        }
        
        if (entity.getAuditStatus() != 0) {
            return ResponseDTO.userErrorParam("该审核记录已处理，无法重复审核");
        }
        
        // 更新审核状态
        entity.setAuditStatus(form.getAuditStatus());
        entity.setAuditOpinion(form.getAuditOpinion());
        entity.setAuditUser(loginUserId);
        entity.setAuditTime(LocalDateTime.now());
        entity.setUpdateBy(loginUserId);
        entity.setUpdateTime(LocalDateTime.now());
        
        spdDataAuditDao.updateById(entity);
        
        String action = form.getAuditStatus() == 1 ? "通过" : "拒绝";
        log.info("审核{}成功，auditId={}, auditType={}, targetId={}", 
            action, entity.getAuditId(), entity.getAuditType(), entity.getTargetId());
        
        return ResponseDTO.ok("审核" + action + "成功");
    }

    /**
     * 撤销审核申请
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> cancelAudit(Long id, String loginUserId) {
        SpdDataAuditEntity entity = spdDataAuditDao.selectById(id);
        if (entity == null || entity.getDelFlag() == 1) {
            return ResponseDTO.userErrorParam("审核记录不存在");
        }
        
        if (entity.getAuditStatus() != 0) {
            return ResponseDTO.userErrorParam("只有待审核状态才能撤销");
        }
        
        if (!entity.getSubmitUser().equals(loginUserId)) {
            return ResponseDTO.userErrorParam("只能撤销自己提交的审核申请");
        }
        
        entity.setAuditStatus(3); // 已撤销
        entity.setUpdateBy(loginUserId);
        entity.setUpdateTime(LocalDateTime.now());
        
        spdDataAuditDao.updateById(entity);
        
        log.info("撤销审核申请成功，auditId={}", entity.getAuditId());
        
        return ResponseDTO.ok("撤销成功");
    }

    /**
     * 生成审核业务ID
     */
    private String generateAuditId() {
        String prefix = "AUDIT";
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        
        Long count = spdDataAuditDao.selectCount(
            new LambdaQueryWrapper<SpdDataAuditEntity>()
                .likeRight(SpdDataAuditEntity::getAuditId, prefix + dateStr)
        );
        
        int newSeq = (count != null ? count.intValue() : 0) + 1;
        return prefix + dateStr + String.format("%06d", newSeq);
    }

    /**
     * 获取审核类型名称
     */
    private String getAuditTypeName(Integer auditType) {
        if (auditType == null) {
            return "";
        }
        return switch (auditType) {
            case 1 -> "供应商";
            case 2 -> "耗材";
            case 3 -> "生产厂家";
            case 4 -> "调价";
            case 5 -> "收费状态";
            default -> "未知";
        };
    }

    /**
     * 获取操作类型名称
     */
    private String getActionTypeName(Integer actionType) {
        if (actionType == null) {
            return "";
        }
        return switch (actionType) {
            case 1 -> "新增";
            case 2 -> "修改";
            default -> "未知";
        };
    }

    /**
     * 获取审核状态名称
     */
    private String getAuditStatusName(Integer auditStatus) {
        if (auditStatus == null) {
            return "";
        }
        return switch (auditStatus) {
            case 0 -> "待审核";
            case 1 -> "已通过";
            case 2 -> "已拒绝";
            case 3 -> "已撤销";
            default -> "未知";
        };
    }
}
