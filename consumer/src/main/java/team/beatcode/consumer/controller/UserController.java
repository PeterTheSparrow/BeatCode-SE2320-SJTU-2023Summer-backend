package team.beatcode.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sjtu.reins.web.utils.Message;
import team.beatcode.consumer.feign.UserFeign;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    @Autowired
    UserFeign userFeign;

    @RequestMapping("/user")
    public Message getUser(@RequestBody Map<String, Object> map) {
        return userFeign.getUser(map);
    };

    @RequestMapping("/register")
    public Message register(@RequestBody Map<String, Object> map) {
        return userFeign.register(map);
    }

    @RequestMapping("/ranks")
    public Message getRanks() {
        return userFeign.getRanks();
    }
}
