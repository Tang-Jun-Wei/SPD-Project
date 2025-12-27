package net.lab1024.sa.admin.module.spd.stocklimit.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.spd.stocklimit.dao.SpdStockLimitDao;
import net.lab1024.sa.admin.module.spd.stocklimit.domain.entity.SpdStockLimitEntity;
import net.lab1024.sa.admin.module.spd.stocklimit.domain.form.SpdStockLimitForm;
import net.lab1024.sa.admin.module.spd.stocklimit.domain.form.SpdStockLimitQueryForm;
import net.lab1024.sa.admin.module.spd.stocklimit.domain.vo.SpdStockLimitVO;
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
public class SpdStockLimitService {

    @Resource
    private SpdStockLimitDao spdStockLimitDao;

    /**
     * 分页查询库存安全量配置
     */
    public ResponseDTO<PageResult<SpdStockLimitVO>> queryPage(SpdStockLimitQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        Page<SpdStockLimitVO> pageData = spdStockLimitDao.queryPage(page, queryForm, 0);
        PageResult<SpdStockLimitVO> pageResult = SmartPageUtil.convert2PageResult(page, pageData.getRecords());
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 查询详情
     */
    public ResponseDTO<SpdStockLimitVO> getDetail(Long id) {
        SpdStockLimitVO vo = spdStockLimitDao.getDetailById(id);
        if (vo == null) {
            return ResponseDTO.userErrorParam("库存安全量配置不存在");
        }
        return ResponseDTO.ok(vo);
    }

    /**
     * 新增库存安全量配置
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> add(SpdStockLimitForm form, String loginUserId, String tenantId) {
        // 验证参数
        if (form.getMinStock() < 0 || form.getMaxStock() < 0) {
            return ResponseDTO.userErrorParam("库存量不能为负数");
        }
        if (form.getMinStock() > form.getMaxStock()) {
            return ResponseDTO.userErrorParam("最小库存量不能大于最大库存量");
        }
        if (form.getWarningStock() != null && (form.getWarningStock() < form.getMinStock() || form.getWarningStock() > form.getMaxStock())) {
            return ResponseDTO.userErrorParam("预警库存量应介于最小库存量和最大库存量之间");
        }

        // 检查是否已存在配置
        Long count = spdStockLimitDao.selectCount(
            new LambdaQueryWrapper<SpdStockLimitEntity>()
                .eq(SpdStockLimitEntity::getMaterialId, form.getMaterialId())
                .eq(SpdStockLimitEntity::getWarehouseId, form.getWarehouseId())
                .eq(SpdStockLimitEntity::getDelFlag, 0)
        );
        if (count > 0) {
            return ResponseDTO.userErrorParam("该耗材在该仓库的安全量配置已存在");
        }

        // 生成业务ID
        String limitId = generateLimitId();

        // 创建实体
        SpdStockLimitEntity entity = SmartBeanUtil.copy(form, SpdStockLimitEntity.class);
        entity.setLimitId(limitId);
        entity.setTenantId(tenantId);
        entity.setDelFlag(0);
        entity.setCreateBy(loginUserId);
        entity.setCreateTime(LocalDateTime.now());

        spdStockLimitDao.insert(entity);

        log.info("新增库存安全量配置成功，limitId={}, materialId={}, warehouseId={}", 
            limitId, form.getMaterialId(), form.getWarehouseId());

        return ResponseDTO.ok("新增成功");
    }

    /**
     * 更新库存安全量配置
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> update(SpdStockLimitForm form, String loginUserId) {
        if (form.getId() == null) {
            return ResponseDTO.userErrorParam("ID不能为空");
        }

        // 验证参数
        if (form.getMinStock() < 0 || form.getMaxStock() < 0) {
            return ResponseDTO.userErrorParam("库存量不能为负数");
        }
        if (form.getMinStock() > form.getMaxStock()) {
            return ResponseDTO.userErrorParam("最小库存量不能大于最大库存量");
        }
        if (form.getWarningStock() != null && (form.getWarningStock() < form.getMinStock() || form.getWarningStock() > form.getMaxStock())) {
            return ResponseDTO.userErrorParam("预警库存量应介于最小库存量和最大库存量之间");
        }

        SpdStockLimitEntity entity = spdStockLimitDao.selectById(form.getId());
        if (entity == null || entity.getDelFlag() == 1) {
            return ResponseDTO.userErrorParam("库存安全量配置不存在");
        }

        // 更新字段
        entity.setMinStock(form.getMinStock());
        entity.setMaxStock(form.getMaxStock());
        entity.setWarningStock(form.getWarningStock());
        entity.setRemark(form.getRemark());
        entity.setUpdateBy(loginUserId);
        entity.setUpdateTime(LocalDateTime.now());

        spdStockLimitDao.updateById(entity);

        log.info("更新库存安全量配置成功，limitId={}", entity.getLimitId());

        return ResponseDTO.ok("更新成功");
    }

    /**
     * 删除库存安全量配置
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> delete(Long id, String loginUserId) {
        SpdStockLimitEntity entity = spdStockLimitDao.selectById(id);
        if (entity == null || entity.getDelFlag() == 1) {
            return ResponseDTO.userErrorParam("库存安全量配置不存在");
        }

        entity.setDelFlag(1);
        entity.setUpdateBy(loginUserId);
        entity.setUpdateTime(LocalDateTime.now());

        spdStockLimitDao.updateById(entity);

        log.info("删除库存安全量配置成功，limitId={}", entity.getLimitId());

        return ResponseDTO.ok("删除成功");
    }

    /**
     * 生成安全量业务ID
     */
    private String generateLimitId() {
        String prefix = "LIMIT";
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        Long count = spdStockLimitDao.selectCount(
            new LambdaQueryWrapper<SpdStockLimitEntity>()
                .likeRight(SpdStockLimitEntity::getLimitId, prefix + dateStr)
        );

        int newSeq = (count != null ? count.intValue() : 0) + 1;
        return prefix + dateStr + String.format("%06d", newSeq);
    }
}
