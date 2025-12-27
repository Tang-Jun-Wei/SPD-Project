package net.lab1024.sa.admin.module.spd.acceptance.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.spd.acceptance.entity.SpdAcceptanceEntity;
import net.lab1024.sa.admin.module.spd.acceptance.domain.form.SpdAcceptanceQueryForm;
import net.lab1024.sa.admin.module.spd.acceptance.domain.vo.SpdAcceptanceVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 验收管理 Dao
 */
@Mapper
public interface SpdAcceptanceDao extends BaseMapper<SpdAcceptanceEntity> {
    
    /**
     * 分页查询
     */
    List<SpdAcceptanceVO> queryPage(Page<?> page, 
                                     @Param("query") SpdAcceptanceQueryForm queryForm,
                                     @Param("delFlag") Integer delFlag);
    
    /**
     * 查询详情
     */
    SpdAcceptanceVO queryDetail(@Param("acceptanceId") String acceptanceId,
                                @Param("delFlag") Integer delFlag);
}
