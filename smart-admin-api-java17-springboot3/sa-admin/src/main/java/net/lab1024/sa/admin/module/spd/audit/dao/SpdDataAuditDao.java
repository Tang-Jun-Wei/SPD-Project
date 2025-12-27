package net.lab1024.sa.admin.module.spd.audit.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.spd.audit.domain.entity.SpdDataAuditEntity;
import net.lab1024.sa.admin.module.spd.audit.domain.form.SpdDataAuditQueryForm;
import net.lab1024.sa.admin.module.spd.audit.domain.vo.SpdDataAuditVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SpdDataAuditDao extends BaseMapper<SpdDataAuditEntity> {

    /**
     * 分页查询审核记录
     */
    Page<SpdDataAuditVO> queryPage(Page page, @Param("query") SpdDataAuditQueryForm query, @Param("delFlag") Integer delFlag);

    /**
     * 查询审核详情
     */
    SpdDataAuditVO getDetailById(@Param("id") Long id);
}
