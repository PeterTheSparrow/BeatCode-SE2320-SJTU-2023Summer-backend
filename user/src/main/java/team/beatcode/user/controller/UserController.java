package team.beatcode.user.controller;

import team.beatcode.user.entity.User;
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

    @RequestMapping("/user")
    public User getUser(@RequestBody Map<String, Object> data)
    {
        // 获得user_id
        Integer userid = (Integer) data.get("userid");
        return userService.getUser(userid);
    }

}
