package team.beatcode.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sjtu.reins.web.utils.Message;
import team.beatcode.consumer.feign.AuthFeign;
import team.beatcode.consumer.interceptors.RequireLogin;

import java.util.Map;

@RestController
public class AuthController {
    @Autowired
    AuthFeign authFeign;

    @RequestMapping("/Login")
    public Message login(@RequestBody Map<String, Object> map) {
        return authFeign.login(map);
    }
    @RequestMapping("/Logout")
    public Message logout() {
        return authFeign.logout();
    }

    @RequestMapping("/Register")
    public Message register(@RequestBody Map<String, Object> map) {
        return authFeign.register(map);
    }
}
