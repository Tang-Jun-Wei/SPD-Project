package net.lab1024.sa.admin.module.spd.batch.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.spd.batch.domain.entity.SpdMaterialBatchEntity;
import net.lab1024.sa.admin.module.spd.batch.domain.form.SpdMaterialBatchQueryForm;
import net.lab1024.sa.admin.module.spd.batch.domain.vo.SpdMaterialBatchVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SpdMaterialBatchDao extends BaseMapper<SpdMaterialBatchEntity> {
    List<SpdMaterialBatchVO> queryPage(Page page, @Param("queryForm") SpdMaterialBatchQueryForm queryForm);
    SpdMaterialBatchVO getDetail(@Param("id") Long id);
    SpdMaterialBatchEntity queryByMaterialAndBatch(@Param("materialId") String materialId, @Param("batchNo") String batchNo, @Param("excludeId") Long excludeId);
}
