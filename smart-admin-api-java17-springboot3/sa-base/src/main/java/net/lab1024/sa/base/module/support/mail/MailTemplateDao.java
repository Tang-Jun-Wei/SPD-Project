package net.lab1024.sa.base.module.support.mail;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.sa.base.module.support.mail.domain.MailTemplateEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 邮件模板
 *
 * @Date 2024/8/5
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a> ，Since 2012
 */
@Mapper
public interface MailTemplateDao extends BaseMapper<MailTemplateEntity> {

}
