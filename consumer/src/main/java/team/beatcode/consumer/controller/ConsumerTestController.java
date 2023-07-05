package team.beatcode.consumer.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.beatcode.consumer.interceptors.RequireLogin;

@RestController
public class ConsumerTestController {

    @RequestMapping("hello")
    @RequireLogin(type = RequireLogin.Type.USER)
    public String hello() {
        return "wow!";
    }
}
