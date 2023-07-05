package team.beatcode.user.controller;

import team.beatcode.user.entity.User;
import team.beatcode.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/user")
    public User getUser(@RequestParam Integer id) {
        return userService.getUser(id);
    }



}
