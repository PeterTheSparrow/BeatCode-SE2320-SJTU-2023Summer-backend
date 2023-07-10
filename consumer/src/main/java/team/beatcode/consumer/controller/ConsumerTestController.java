package team.beatcode.consumer.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.beatcode.consumer.interceptors.RequireLogin;
import team.beatcode.consumer.utils.context.UserContext;
import team.beatcode.consumer.utils.context.UserContextHolder;

@RestController
public class ConsumerTestController {

    @RequestMapping("hello")
    @RequireLogin(type = RequireLogin.Type.USER)
    public UserContext hello() {
        return UserContextHolder.getUserAccount();
    }
}
