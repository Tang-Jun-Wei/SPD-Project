package net.lab1024.sa.admin.module.spd.material.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.spd.material.dao.SpdMaterialDao;
import net.lab1024.sa.admin.module.spd.material.domain.form.SpdMaterialForm;
import net.lab1024.sa.admin.module.spd.material.domain.form.SpdMaterialQueryForm;
import net.lab1024.sa.admin.module.spd.material.domain.vo.SpdMaterialVO;
import net.lab1024.sa.admin.module.spd.material.entity.SpdMaterialEntity;
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

/**
 * 耗材信息Service
 */
@Slf4j
@Service
public class SpdMaterialService {

    @Autowired
    private SpdMaterialDao spdMaterialDao;

    /**
     * 分页查询
     */
    public PageResult<SpdMaterialVO> queryPage(SpdMaterialQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<SpdMaterialVO> list = spdMaterialDao.queryPage(page, queryForm, 0);
        return SmartPageUtil.convert2PageResult(page, list);
    }

    /**
     * 新增耗材
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> add(SpdMaterialForm form, String loginUserId, String tenantId) {
        // 1. 校验耗材名称+生产厂家唯一性
        SpdMaterialEntity existMaterial = spdMaterialDao.queryByNameAndManufacturer(
            form.getMaterialName(), form.getManufacturer(), null, 0);
        if (existMaterial != null) {
            return ResponseDTO.userErrorParam("相同名称和生产厂家的耗材已存在");
        }

        // 2. 生成耗材业务ID
        String materialId = generateMaterialId();

        // 3. 构建实体对象
        SpdMaterialEntity entity = SmartBeanUtil.copy(form, SpdMaterialEntity.class);
        entity.setMaterialId(materialId);
        entity.setTenantId(tenantId);
        entity.setDelFlag(0);
        entity.setMaterialStatus(form.getMaterialStatus() != null ? form.getMaterialStatus() : 1);
        entity.setCreateBy(loginUserId);
        entity.setCreateTime(LocalDateTime.now());

        // 4. 保存到数据库
        spdMaterialDao.insert(entity);

        log.info("新增耗材成功，materialId={}", materialId);
        return ResponseDTO.ok("新增成功");
    }

    /**
     * 修改耗材
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> update(SpdMaterialForm form, String loginUserId) {
        // 1. 校验是否存在
        if (form.getId() == null) {
            return ResponseDTO.userErrorParam("ID不能为空");
        }
        
        SpdMaterialEntity existEntity = spdMaterialDao.selectById(form.getId());
        if (existEntity == null || existEntity.getDelFlag() == 1) {
            return ResponseDTO.userErrorParam("耗材不存在");
        }

        // 2. 校验名称+生产厂家唯一性
        SpdMaterialEntity duplicateMaterial = spdMaterialDao.queryByNameAndManufacturer(
            form.getMaterialName(), form.getManufacturer(), form.getId(), 0);
        if (duplicateMaterial != null) {
            return ResponseDTO.userErrorParam("相同名称和生产厂家的耗材已存在");
        }

        // 3. 更新实体对象
        SpdMaterialEntity entity = SmartBeanUtil.copy(form, SpdMaterialEntity.class);
        entity.setId(form.getId());
        entity.setUpdateBy(loginUserId);
        entity.setUpdateTime(LocalDateTime.now());

        spdMaterialDao.updateById(entity);

        log.info("修改耗材成功，id={}", form.getId());
        return ResponseDTO.ok("修改成功");
    }

    /**
     * 删除耗材（逻辑删除）
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> delete(Long id, String loginUserId) {
        SpdMaterialEntity entity = spdMaterialDao.selectById(id);
        if (entity == null || entity.getDelFlag() == 1) {
            return ResponseDTO.userErrorParam("耗材不存在");
        }

        entity.setDelFlag(1);
        entity.setUpdateBy(loginUserId);
        entity.setUpdateTime(LocalDateTime.now());
        spdMaterialDao.updateById(entity);

        log.info("删除耗材成功，id={}", id);
        return ResponseDTO.ok("删除成功");
    }

    /**
     * 生成耗材业务ID
     * 格式：MAT + 年月日 + 6位序号
     */
    private String generateMaterialId() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        Long count = spdMaterialDao.selectCount(
            new LambdaQueryWrapper<SpdMaterialEntity>()
                .likeRight(SpdMaterialEntity::getMaterialId, "MAT" + dateStr)
        );
        int newSeq = (count != null ? count.intValue() : 0) + 1;
        return "MAT" + dateStr + String.format("%06d", newSeq);
    }
}
