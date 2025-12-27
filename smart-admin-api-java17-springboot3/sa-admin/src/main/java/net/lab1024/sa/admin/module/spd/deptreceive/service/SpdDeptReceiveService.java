package net.lab1024.sa.admin.module.spd.deptreceive.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.spd.deptreceive.dao.SpdDeptReceiveDao;
import net.lab1024.sa.admin.module.spd.deptreceive.dao.SpdDeptReceiveDetailDao;
import net.lab1024.sa.admin.module.spd.deptreceive.domain.entity.SpdDeptReceiveEntity;
import net.lab1024.sa.admin.module.spd.deptreceive.domain.vo.SpdDeptReceiveVO;
import net.lab1024.sa.base.common.domain.PageParam;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 科室收货服务类
 */
@Service
public class SpdDeptReceiveService {
    
    @Resource
    private SpdDeptReceiveDao spdDeptReceiveDao;
    
    @Resource
    private SpdDeptReceiveDetailDao spdDeptReceiveDetailDao;
    
    /**
     * 分页查询科室收货单
     */
    public ResponseDTO<PageResult<SpdDeptReceiveVO>> queryPage(PageParam pageParam) {
        Page<?> page = SmartPageUtil.convert2PageQuery(pageParam);
        Page<SpdDeptReceiveEntity> resultPage = spdDeptReceiveDao.selectPage(
            (Page<SpdDeptReceiveEntity>) page, 
            new LambdaQueryWrapper<SpdDeptReceiveEntity>().orderByDesc(SpdDeptReceiveEntity::getCreateTime)
        );
        List<SpdDeptReceiveVO> voList = SmartBeanUtil.copyList(resultPage.getRecords(), SpdDeptReceiveVO.class);
        PageResult<SpdDeptReceiveVO> pageResult = SmartPageUtil.convert2PageResult(resultPage, voList);
        return ResponseDTO.ok(pageResult);
    }
    
    /**
     * 查询收货单详情
     */
    public ResponseDTO<SpdDeptReceiveVO> getDetail(String receiveId) {
        SpdDeptReceiveEntity entity = spdDeptReceiveDao.selectOne(
            new LambdaQueryWrapper<SpdDeptReceiveEntity>()
                .eq(SpdDeptReceiveEntity::getReceiveId, receiveId)
        );
        if (entity == null) {
            return ResponseDTO.userErrorParam("收货单不存在");
        }
        SpdDeptReceiveVO vo = SmartBeanUtil.copy(entity, SpdDeptReceiveVO.class);
        return ResponseDTO.ok(vo);
    }
    
    /**
     * 确认收货
     */
    public ResponseDTO<String> confirmReceive(String receiveId, String userId) {
        SpdDeptReceiveEntity entity = spdDeptReceiveDao.selectOne(
            new LambdaQueryWrapper<SpdDeptReceiveEntity>()
                .eq(SpdDeptReceiveEntity::getReceiveId, receiveId)
        );
        if (entity == null) {
            return ResponseDTO.userErrorParam("收货单不存在");
        }
        
        entity.setReceiveStatus(2); // 已收货
        entity.setReceiveBy(userId);
        entity.setReceiveTime(LocalDateTime.now());
        spdDeptReceiveDao.updateById(entity);
        
        return ResponseDTO.ok("收货成功");
    }
    
    /**
     * 拒收
     */
    public ResponseDTO<String> rejectReceive(String receiveId, String reason, String userId) {
        SpdDeptReceiveEntity entity = spdDeptReceiveDao.selectOne(
            new LambdaQueryWrapper<SpdDeptReceiveEntity>()
                .eq(SpdDeptReceiveEntity::getReceiveId, receiveId)
        );
        if (entity == null) {
            return ResponseDTO.userErrorParam("收货单不存在");
        }
        
        entity.setReceiveStatus(3); // 部分拒收
        entity.setRemark(reason);
        entity.setReceiveBy(userId);
        entity.setReceiveTime(LocalDateTime.now());
        spdDeptReceiveDao.updateById(entity);
        
        return ResponseDTO.ok("拒收成功");
    }
}
