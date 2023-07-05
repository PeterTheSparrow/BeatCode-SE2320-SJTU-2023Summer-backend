package team.beatcode.user.controller;

import team.beatcode.user.entity.User;
import team.beatcode.user.entity.User_record;
import team.beatcode.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/user")
    public User getUser(@RequestBody Map<String, Object> data) {
        Integer userId = (Integer) data.get("userId");

        return userService.getUser(userId);
    }

//    @GetMapping("/ranks")
//    public List<User_record> getRanks() {
//        return userService.getRanks();
//    }


}
