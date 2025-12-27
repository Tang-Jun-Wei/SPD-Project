package net.lab1024.sa.admin.module.spd.receive.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.spd.receive.domain.entity.SpdReceiveMainEntity;
import net.lab1024.sa.admin.module.spd.receive.domain.form.SpdReceiveQueryForm;
import net.lab1024.sa.admin.module.spd.receive.domain.vo.SpdReceiveVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SpdReceiveDao extends BaseMapper<SpdReceiveMainEntity> {
    List<SpdReceiveVO> queryPage(Page<?> page, @Param("query") SpdReceiveQueryForm queryForm, @Param("delFlag") Integer delFlag);
    
    SpdReceiveVO getDetailByCode(@Param("receiveCode") String receiveCode, @Param("delFlag") Integer delFlag);
}
