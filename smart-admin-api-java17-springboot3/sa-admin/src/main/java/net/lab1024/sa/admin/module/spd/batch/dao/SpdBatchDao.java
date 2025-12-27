package net.lab1024.sa.admin.module.spd.batch.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.spd.batch.entity.SpdBatchEntity;
import net.lab1024.sa.admin.module.spd.batch.domain.form.SpdBatchQueryForm;
import net.lab1024.sa.admin.module.spd.batch.domain.vo.SpdBatchVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 批号信息Dao接口
 */
@Mapper
public interface SpdBatchDao extends BaseMapper<SpdBatchEntity> {

    /**
     * 分页查询
     */
    List<SpdBatchVO> queryPage(Page<?> page, @Param("query") SpdBatchQueryForm queryForm, @Param("delFlag") Integer delFlag);

    /**
     * 根据批号+耗材ID查询（用于唯一性校验）
     */
    SpdBatchEntity queryByBatchNoAndMaterialId(@Param("batchNo") String batchNo,
                                                @Param("materialId") String materialId,
                                                @Param("id") Long id,
                                                @Param("delFlag") Integer delFlag);

    /**
     * 根据业务ID查询
     */
    SpdBatchEntity queryByBatchId(@Param("batchId") String batchId, @Param("delFlag") Integer delFlag);
}
