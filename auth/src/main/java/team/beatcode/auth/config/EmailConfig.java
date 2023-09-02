package team.beatcode.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
public class EmailConfig {
    /*
    * 发送邮件服务器
    * */
    @Value("${spring.mail.host}")
    private String host;

    /*
    * stmp端口
    * stmp是发送邮件的协议
    * */
    @Value("${spring.mail.smtp.port}")
    private String port;

    /*
    * 发送邮件的邮箱
    */
    @Value("${spring.mail.from}")
    private String from;

    /*
    * 发送邮件的邮箱地址
    * */
    @Value("${spring.mail.user}")
    private String user;

    /*
    * 客户端授权码
    * */
    @Value("${spring.mail.pass}")
    private String pass;
}
