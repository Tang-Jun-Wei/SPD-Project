package net.lab1024.sa.admin.module.spd.label.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.spd.label.dao.SpdLabelDao;
import net.lab1024.sa.admin.module.spd.label.entity.SpdLabelEntity;
import net.lab1024.sa.admin.module.spd.label.domain.form.SpdLabelForm;
import net.lab1024.sa.admin.module.spd.label.domain.form.SpdLabelQueryForm;
import net.lab1024.sa.admin.module.spd.label.domain.vo.SpdLabelVO;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Slf4j
@Service
public class SpdLabelService {

    @Autowired
    private SpdLabelDao spdLabelDao;

    public PageResult<SpdLabelVO> queryPage(SpdLabelQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        var list = spdLabelDao.queryPage(page, queryForm, 0);
        return SmartPageUtil.convert2PageResult(page, list);
    }

    public ResponseDTO<SpdLabelVO> getDetail(String labelCode) {
        SpdLabelVO vo = spdLabelDao.getDetailByCode(labelCode, 0);
        if (vo == null) return ResponseDTO.userErrorParam("标签不存在");
        return ResponseDTO.ok(vo);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> add(SpdLabelForm form, String loginUserId, String tenantId) {
        SpdLabelEntity exist = spdLabelDao.selectOne(
            new LambdaQueryWrapper<SpdLabelEntity>()
                .eq(SpdLabelEntity::getLabelCode, form.getLabelCode())
                .eq(SpdLabelEntity::getDelFlag, 0));
        if (exist != null) return ResponseDTO.userErrorParam("标签码已存在");
        
        SpdLabelEntity entity = SmartBeanUtil.copy(form, SpdLabelEntity.class);
        entity.setLabelStatus(1);
        entity.setCreateTime(LocalDateTime.now());
        entity.setTenantId(tenantId);
        entity.setDelFlag(0);
        spdLabelDao.insert(entity);

        log.info("新增标签成功，labelCode={}", form.getLabelCode());
        return ResponseDTO.ok("新增成功");
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> update(SpdLabelForm form, String loginUserId) {
        SpdLabelEntity entity = spdLabelDao.selectById(form.getId());
        if (entity == null || entity.getDelFlag() == 1) return ResponseDTO.userErrorParam("标签不存在");
        if (entity.getLabelStatus() != 1) return ResponseDTO.userErrorParam("只能修改可用状态的标签");

        SmartBeanUtil.copyProperties(form, entity);
        entity.setUpdateBy(loginUserId);
        entity.setUpdateTime(LocalDateTime.now());
        spdLabelDao.updateById(entity);

        log.info("更新标签成功，labelCode={}", entity.getLabelCode());
        return ResponseDTO.ok("更新成功");
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> delete(String labelCode, String loginUserId) {
        SpdLabelEntity entity = spdLabelDao.selectOne(
            new LambdaQueryWrapper<SpdLabelEntity>()
                .eq(SpdLabelEntity::getLabelCode, labelCode)
                .eq(SpdLabelEntity::getDelFlag, 0));
        if (entity == null) return ResponseDTO.userErrorParam("标签不存在");

        entity.setDelFlag(1);
        entity.setUpdateBy(loginUserId);
        entity.setUpdateTime(LocalDateTime.now());
        spdLabelDao.updateById(entity);

        log.info("删除标签成功，labelCode={}", labelCode);
        return ResponseDTO.ok("删除成功");
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> use(String labelCode, String loginUserId) {
        SpdLabelEntity entity = spdLabelDao.selectOne(
            new LambdaQueryWrapper<SpdLabelEntity>()
                .eq(SpdLabelEntity::getLabelCode, labelCode)
                .eq(SpdLabelEntity::getDelFlag, 0));
        if (entity == null) return ResponseDTO.userErrorParam("标签不存在");
        if (entity.getLabelStatus() != 1) return ResponseDTO.userErrorParam("该标签不可用");

        entity.setLabelStatus(2);
        entity.setUseTime(LocalDateTime.now());
        entity.setUpdateBy(loginUserId);
        entity.setUpdateTime(LocalDateTime.now());
        spdLabelDao.updateById(entity);

        log.info("标签领用成功，labelCode={}", labelCode);
        return ResponseDTO.ok("领用成功");
    }
}
