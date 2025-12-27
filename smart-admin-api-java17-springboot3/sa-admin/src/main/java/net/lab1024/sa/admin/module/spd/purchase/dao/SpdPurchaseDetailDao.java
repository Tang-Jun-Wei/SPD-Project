package net.lab1024.sa.admin.module.spd.purchase.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.sa.admin.module.spd.purchase.entity.SpdPurchaseDetailEntity;
import net.lab1024.sa.admin.module.spd.purchase.domain.vo.SpdPurchaseDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SpdPurchaseDetailDao extends BaseMapper<SpdPurchaseDetailEntity> {
    List<SpdPurchaseDetailVO> queryByPurchaseId(@Param("purchaseId") String purchaseId, @Param("delFlag") Integer delFlag);
}
