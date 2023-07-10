package team.beatcode.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sjtu.reins.web.utils.Message;
import team.beatcode.consumer.feign.AuthFeign;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
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

    @RequestMapping("/GetProblemList")
    public Message getProblemList() {
        return authFeign.getProblemList();
    }

    @RequestMapping("/CheckUser")
    public Message checkUser() {
        return authFeign.checkUser();
    }

    @RequestMapping("/CheckAdmin")
    public Message checkAdmin() {
        return authFeign.checkAdmin();
    }
}
