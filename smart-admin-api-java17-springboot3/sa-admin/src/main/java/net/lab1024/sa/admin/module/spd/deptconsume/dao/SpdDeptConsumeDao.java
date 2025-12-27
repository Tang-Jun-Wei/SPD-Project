package net.lab1024.sa.admin.module.spd.deptconsume.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.spd.deptconsume.domain.entity.SpdDeptConsumeEntity;
import net.lab1024.sa.admin.module.spd.deptconsume.domain.form.SpdDeptConsumeQueryForm;
import net.lab1024.sa.admin.module.spd.deptconsume.domain.vo.SpdDeptConsumeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 科室消耗DAO
 *
 * @author 1024创新实验室
 */
@Mapper
public interface SpdDeptConsumeDao extends BaseMapper<SpdDeptConsumeEntity> {

    /**
     * 分页查询
     */
    Page<SpdDeptConsumeVO> queryPage(Page page, @Param("query") SpdDeptConsumeQueryForm queryForm);
}
