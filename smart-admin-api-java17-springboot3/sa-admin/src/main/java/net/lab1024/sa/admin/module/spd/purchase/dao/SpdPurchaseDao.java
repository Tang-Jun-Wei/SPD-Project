package net.lab1024.sa.admin.module.spd.purchase.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.spd.purchase.entity.SpdPurchaseEntity;
import net.lab1024.sa.admin.module.spd.purchase.domain.form.SpdPurchaseQueryForm;
import net.lab1024.sa.admin.module.spd.purchase.domain.vo.SpdPurchaseVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SpdPurchaseDao extends BaseMapper<SpdPurchaseEntity> {
    List<SpdPurchaseVO> queryPage(Page<?> page, @Param("query") SpdPurchaseQueryForm queryForm, @Param("delFlag") Integer delFlag);
    SpdPurchaseVO getDetailById(@Param("id") Long id, @Param("delFlag") Integer delFlag);
}
