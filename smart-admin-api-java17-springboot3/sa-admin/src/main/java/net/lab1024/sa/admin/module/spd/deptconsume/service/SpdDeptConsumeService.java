package net.lab1024.sa.admin.module.spd.deptconsume.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.spd.deptconsume.dao.SpdDeptConsumeDao;
import net.lab1024.sa.admin.module.spd.deptconsume.dao.SpdDeptConsumeDetailDao;
import net.lab1024.sa.admin.module.spd.deptconsume.domain.entity.SpdDeptConsumeDetailEntity;
import net.lab1024.sa.admin.module.spd.deptconsume.domain.entity.SpdDeptConsumeEntity;
import net.lab1024.sa.admin.module.spd.deptconsume.domain.form.SpdDeptConsumeAddForm;
import net.lab1024.sa.admin.module.spd.deptconsume.domain.form.SpdDeptConsumeDetailAddForm;
import net.lab1024.sa.admin.module.spd.deptconsume.domain.form.SpdDeptConsumeQueryForm;
import net.lab1024.sa.admin.module.spd.deptconsume.domain.vo.SpdDeptConsumeDetailVO;
import net.lab1024.sa.admin.module.spd.deptconsume.domain.vo.SpdDeptConsumeVO;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 科室消耗Service
 *
 * @author 1024创新实验室
 */
@Slf4j
@Service
public class SpdDeptConsumeService {

    @Autowired
    private SpdDeptConsumeDao spdDeptConsumeDao;

    @Autowired
    private SpdDeptConsumeDetailDao spdDeptConsumeDetailDao;

    /**
     * 分页查询
     */
    public ResponseDTO<PageResult<SpdDeptConsumeVO>> queryPage(SpdDeptConsumeQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<SpdDeptConsumeVO> list = spdDeptConsumeDao.queryPage(page, queryForm).getRecords();
        PageResult<SpdDeptConsumeVO> pageResultVO = SmartPageUtil.convert2PageResult(page, list);
        return ResponseDTO.ok(pageResultVO);
    }

    /**
     * 查询详情
     */
    public ResponseDTO<SpdDeptConsumeVO> getDetail(Long consumeId) {
        SpdDeptConsumeEntity entity = spdDeptConsumeDao.selectById(consumeId);
        if (entity == null) {
            return ResponseDTO.userErrorParam("消耗单不存在");
        }
        SpdDeptConsumeVO vo = SmartBeanUtil.copy(entity, SpdDeptConsumeVO.class);
        // 查询明细
        List<SpdDeptConsumeDetailVO> detailList = spdDeptConsumeDetailDao.selectByConsumeId(consumeId);
        vo.setDetailList(detailList);
        return ResponseDTO.ok(vo);
    }

    /**
     * 新增消耗
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> add(SpdDeptConsumeAddForm addForm, Long userId, String userName) {
        // 生成消耗单号
        String consumeNo = generateConsumeNo();

        // 计算总数量和总金额
        BigDecimal totalQuantity = BigDecimal.ZERO;
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (SpdDeptConsumeDetailAddForm detail : addForm.getDetailList()) {
            totalQuantity = totalQuantity.add(detail.getQuantity());
            if (detail.getAmount() != null) {
                totalAmount = totalAmount.add(detail.getAmount());
            }
        }

        // 保存主表
        SpdDeptConsumeEntity entity = SmartBeanUtil.copy(addForm, SpdDeptConsumeEntity.class);
        entity.setConsumeNo(consumeNo);
        entity.setTotalQuantity(totalQuantity);
        entity.setTotalAmount(totalAmount);
        entity.setConsumeUserId(userId);
        entity.setConsumeUserName(userName);
        entity.setStatus(1); // 正常
        entity.setCreateUserId(userId);
        entity.setCreateUserName(userName);
        entity.setCreateTime(LocalDateTime.now());
        spdDeptConsumeDao.insert(entity);

        // 保存明细
        for (SpdDeptConsumeDetailAddForm detailForm : addForm.getDetailList()) {
            SpdDeptConsumeDetailEntity detailEntity = SmartBeanUtil.copy(detailForm, SpdDeptConsumeDetailEntity.class);
            detailEntity.setConsumeId(entity.getConsumeId());
            detailEntity.setIsReversed(0); // 未反消耗
            detailEntity.setCreateTime(LocalDateTime.now());
            spdDeptConsumeDetailDao.insert(detailEntity);
        }

        // TODO: 扣减库存逻辑

        return ResponseDTO.ok();
    }

    /**
     * 反消耗
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> reverse(Long consumeId, String reason, Long userId, String userName) {
        // 查询消耗单
        SpdDeptConsumeEntity entity = spdDeptConsumeDao.selectById(consumeId);
        if (entity == null) {
            return ResponseDTO.userErrorParam("消耗单不存在");
        }
        if (entity.getStatus() == 2) {
            return ResponseDTO.userErrorParam("消耗单已反消耗");
        }

        // 更新主表状态
        entity.setStatus(2); // 已反消耗
        entity.setRemark(reason);
        entity.setUpdateTime(LocalDateTime.now());
        spdDeptConsumeDao.updateById(entity);

        // 更新明细反消耗信息
        List<SpdDeptConsumeDetailVO> detailList = spdDeptConsumeDetailDao.selectByConsumeId(consumeId);
        for (SpdDeptConsumeDetailVO detail : detailList) {
            SpdDeptConsumeDetailEntity detailEntity = new SpdDeptConsumeDetailEntity();
            detailEntity.setDetailId(detail.getDetailId());
            detailEntity.setIsReversed(1); // 已反消耗
            detailEntity.setReversedTime(LocalDateTime.now());
            detailEntity.setReversedUserId(userId);
            detailEntity.setReversedUserName(userName);
            spdDeptConsumeDetailDao.updateById(detailEntity);
        }

        // TODO: 回滚库存逻辑

        return ResponseDTO.ok();
    }

    /**
     * 删除
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> delete(Long consumeId) {
        SpdDeptConsumeEntity entity = spdDeptConsumeDao.selectById(consumeId);
        if (entity == null) {
            return ResponseDTO.userErrorParam("消耗单不存在");
        }
        // 逻辑删除
        spdDeptConsumeDao.deleteById(consumeId);
        return ResponseDTO.ok();
    }

    /**
     * 生成消耗单号
     */
    private String generateConsumeNo() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return "XH" + date + System.currentTimeMillis() % 100000;
    }
}
