package team.beatcode.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.beatcode.consumer.feign.UserFeign;
import team.beatcode.user.entity.User;
import team.beatcode.user.entity.User_record;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    UserFeign userFeign;

    @RequestMapping("/user")
    public User getUser(@RequestBody Map<String, Object> map) {
        return userFeign.getUser(map);
    };

    @RequestMapping("/register")
    public void register(@RequestBody Map<String, Object> map) {
        userFeign.register(map);
    }

    @RequestMapping("/ranks")
    public List<User_record> getRanks() {
        return userFeign.getRanks();
    }
}
