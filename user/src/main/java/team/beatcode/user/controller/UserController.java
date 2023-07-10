package team.beatcode.user.controller;

import team.beatcode.user.entity.User;
import team.beatcode.user.entity.User_record;
import team.beatcode.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping("/user")
    public User getUser(@RequestBody Map<String, Object> data) {
        Integer userId = (Integer) data.get("userId");

        return userService.getUser(userId);
    }

    @RequestMapping("/register")
    public void register(@RequestBody Map<String, Object> data) {
        Integer userId = (Integer) data.get("user_id");
        String userName = (String) data.get("name");
        String email = (String) data.get("email");
        String phone = (String) data.get("phone");

        userService.register(userId, userName, email, phone);
    }

    @RequestMapping("/ranks")
    public List<User_record> getRanks() {
        return userService.getRanks();
    }
}
