package net.lab1024.sa.admin.module.spd.allocation.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.spd.allocation.domain.entity.SpdDeptAllocationEntity;
import net.lab1024.sa.admin.module.spd.allocation.domain.vo.SpdDeptAllocationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SpdDeptAllocationDao extends BaseMapper<SpdDeptAllocationEntity> {
    Page<SpdDeptAllocationVO> queryPage(Page<SpdDeptAllocationVO> page, @Param("form") Object form);
    SpdDeptAllocationVO getDetail(@Param("allocationId") String allocationId);
}
