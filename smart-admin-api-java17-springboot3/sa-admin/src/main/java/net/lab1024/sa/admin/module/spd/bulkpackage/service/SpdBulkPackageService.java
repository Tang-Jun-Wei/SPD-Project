package net.lab1024.sa.admin.module.spd.bulkpackage.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.spd.bulkpackage.dao.SpdBulkPackageDao;
import net.lab1024.sa.admin.module.spd.bulkpackage.dao.SpdBulkPackageDetailDao;
import net.lab1024.sa.admin.module.spd.bulkpackage.domain.entity.SpdBulkPackageDetailEntity;
import net.lab1024.sa.admin.module.spd.bulkpackage.domain.entity.SpdBulkPackageEntity;
import net.lab1024.sa.admin.module.spd.bulkpackage.domain.form.SpdBulkPackageAddForm;
import net.lab1024.sa.admin.module.spd.bulkpackage.domain.form.SpdBulkPackageQueryForm;
import net.lab1024.sa.admin.module.spd.bulkpackage.domain.vo.SpdBulkPackageDetailVO;
import net.lab1024.sa.admin.module.spd.bulkpackage.domain.vo.SpdBulkPackageVO;
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
import java.util.ArrayList;
import java.util.List;

/**
 * 散货打包Service
 */
@Slf4j
@Service
public class SpdBulkPackageService {

    @Autowired
    private SpdBulkPackageDao spdBulkPackageDao;

    @Autowired
    private SpdBulkPackageDetailDao spdBulkPackageDetailDao;

    /**
     * 分页查询
     */
    public ResponseDTO<PageResult<SpdBulkPackageVO>> queryPage(SpdBulkPackageQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        Page<SpdBulkPackageVO> pageResult = spdBulkPackageDao.queryPage(page, queryForm);
        return ResponseDTO.ok(SmartPageUtil.convert2PageResult(pageResult));
    }

    /**
     * 查询详情
     */
    public ResponseDTO<SpdBulkPackageVO> getDetail(Long packageId) {
        SpdBulkPackageEntity entity = spdBulkPackageDao.selectById(packageId);
        if (entity == null) {
            return ResponseDTO.userErrorParam("打包单不存在");
        }
        SpdBulkPackageVO vo = SmartBeanUtil.copy(entity, SpdBulkPackageVO.class);
        List<SpdBulkPackageDetailVO> detailList = spdBulkPackageDetailDao.selectByPackageId(packageId);
        vo.setDetailList(detailList);
        return ResponseDTO.ok(vo);
    }

    /**
     * 新增打包
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> add(SpdBulkPackageAddForm addForm, Long userId, String userName) {
        // 生成打包单号
        String packageNo = generatePackageNo();

        // 计算总数量
        BigDecimal totalQuantity = addForm.getUnitQuantity().multiply(new BigDecimal(addForm.getPackageQuantity()));

        // 保存主表
        SpdBulkPackageEntity entity = SmartBeanUtil.copy(addForm, SpdBulkPackageEntity.class);
        entity.setPackageNo(packageNo);
        entity.setTotalQuantity(totalQuantity);
        entity.setStatus(1); // 待审核
        entity.setCreateUserId(userId);
        entity.setCreateUserName(userName);
        entity.setCreateTime(LocalDateTime.now());
        spdBulkPackageDao.insert(entity);

        // 生成标签并保存明细
        List<SpdBulkPackageDetailEntity> detailList = new ArrayList<>();
        for (int i = 0; i < addForm.getPackageQuantity(); i++) {
            SpdBulkPackageDetailEntity detailEntity = new SpdBulkPackageDetailEntity();
            detailEntity.setPackageId(entity.getPackageId());
            detailEntity.setLabelCode(generateLabelCode());
            detailEntity.setSerialNo(generateSerialNo());
            detailEntity.setQuantity(addForm.getUnitQuantity());
            detailEntity.setStatus(1); // 正常
            detailEntity.setCreateTime(LocalDateTime.now());
            detailList.add(detailEntity);
        }

        // 批量插入明细
        detailList.forEach(spdBulkPackageDetailDao::insert);

        // TODO: 扣减散货库存

        return ResponseDTO.ok();
    }

    /**
     * 删除
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> delete(Long packageId) {
        SpdBulkPackageEntity entity = spdBulkPackageDao.selectById(packageId);
        if (entity == null) {
            return ResponseDTO.userErrorParam("打包单不存在");
        }
        spdBulkPackageDao.deleteById(packageId);
        return ResponseDTO.ok();
    }

    /**
     * 生成打包单号
     */
    private String generatePackageNo() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return "BP" + date + System.currentTimeMillis() % 100000;
    }

    /**
     * 生成标签编码
     */
    private String generateLabelCode() {
        return "LB" + System.currentTimeMillis() + (int)(Math.random() * 1000);
    }

    /**
     * 生成序列号
     */
    private String generateSerialNo() {
        return "SN" + System.currentTimeMillis() + (int)(Math.random() * 1000);
    }
}
