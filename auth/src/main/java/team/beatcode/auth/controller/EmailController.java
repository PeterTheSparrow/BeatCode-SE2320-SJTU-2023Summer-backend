package team.beatcode.auth.controller;

import cn.hutool.extra.mail.MailAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.beatcode.auth.config.EmailConfig;
import team.beatcode.auth.feign.UserFeign;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/emails")
public class EmailController {
    @Autowired
    private EmailConfig emailConfig;

    @Autowired
    private UserFeign userFeign;

    @RequestMapping("/send")
    public String send(@RequestParam String email, @RequestParam String code) {
        // wrapping para
        Map<String, Object> map = new HashMap<>();
        map.put("email", email);

        // 检查邮箱是否已经注册
        if (userFeign.checkEmailExist(map)) {
            return "exist";
        }

        boolean result = sendEmail(email, code);

        if (result) {
            return "success";
        } else {
            return "fail";
        }
    }

    private boolean sendEmail(String email, String verificationCode) {
        try{
            MailAccount account = new MailAccount();
            account.setHost(emailConfig.getHost());
            account.setPort(Integer.parseInt(emailConfig.getPort()));
            account.setFrom(emailConfig.getFrom());
            account.setUser(emailConfig.getUser());
            account.setPass(emailConfig.getPass());

            /*
            * 您好，这是beatcode的账号激活邮件。您的验证码是：verificationCode。有效期：10分钟
            * */
            cn.hutool.extra.mail.MailUtil.send(account, email, "beatcode账号激活邮件", "您好，这是beatcode的账号激活邮件。您的验证码是：" + verificationCode + "。有效期：10分钟", false);

            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}
