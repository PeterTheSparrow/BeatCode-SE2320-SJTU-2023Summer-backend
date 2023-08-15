package team.beatcode.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import team.beatcode.auth.Service.CodeService;

@RestController
@RequestMapping("/codes")
public class CodeController {
    @Autowired
    private CodeService codeService;

    /**
     * 生成验证码
     * @param email 邮箱地址
     *
     * @return success：发送成功；fail：发送失败
     * */
    @RequestMapping("/create")
    public String create(@RequestParam String email) {
        // 生成验证码
        String verificationCode = codeService.createCode(email);

        // 将验证码发送到用户的邮箱
        return sendEmail(email, verificationCode);
    }

    /**
     * 检查验证码是否合法
     * @param email 邮箱地址
     * @param code  验证码
     *
     * @return 0：验证码正确；1：验证码错误；2：验证码过期
     * */
    @RequestMapping("/check")
    public Integer check(@RequestParam String email, @RequestParam String code) {
        // 检查验证码是否正确
        return codeService.checkCode(email, code);
    }

    /**
     * 发送邮件
     * @param email 邮箱地址
     * @param verificationCode  验证码
     *
     * @return success：发送成功；fail：发送失败
     * */
    private String sendEmail(String email, String verificationCode) {
        String url = "http://localhost:8762/emails/send?email=" + email + "&code=" + verificationCode;
        // 发送邮件
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.getForObject(url, String.class);
    }
}

/*
* TODO: 关于邮箱验证服务
*  1. 首先需要确定的是整个运作的模式，是在注册的时候验证，还是用户在登录之后验证邮箱是合法的
*     前者需要修改注册服务
*     后者需要前端稍微多做一点东西
*  2. 【但都要做的是】，把check和create两个服务都到注册中心去注册
* */
