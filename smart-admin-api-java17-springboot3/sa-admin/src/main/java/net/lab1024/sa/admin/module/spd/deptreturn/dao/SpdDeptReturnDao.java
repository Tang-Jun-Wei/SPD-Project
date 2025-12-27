package net.lab1024.sa.admin.module.spd.deptreturn.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.spd.deptreturn.domain.entity.SpdDeptReturnEntity;
import net.lab1024.sa.admin.module.spd.deptreturn.domain.vo.SpdDeptReturnVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SpdDeptReturnDao extends BaseMapper<SpdDeptReturnEntity> {
    /**
     * 分页查询退库单列表
     */
    Page<SpdDeptReturnVO> queryPage(Page<SpdDeptReturnVO> page, @Param("form") Object form);

    /**
     * 查询退库单详情
     */
    SpdDeptReturnVO getDetail(@Param("returnId") String returnId);
}
