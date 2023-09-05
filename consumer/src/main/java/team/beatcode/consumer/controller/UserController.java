package team.beatcode.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sjtu.reins.web.utils.Message;
import team.beatcode.consumer.feign.UserFeign;
import team.beatcode.user.entity.Person_info;
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
    }

    @RequestMapping("/register")
    public void register(@RequestBody Map<String, Object> map) {
        userFeign.register(map);
    }

    @RequestMapping("/getRanking")
    public Map<String,Object> getRanking(@RequestBody Map<String,Object> map) {
        return userFeign.getRanking(map);
    }

    @RequestMapping("/checkUserExist")
    public Boolean checkUserExist(@RequestBody Map<String, Object> map) {return userFeign.checkUserExist(map);}

    @RequestMapping("/getUserInfo")
    public Person_info getUserInfo(@RequestBody Map<String, Object> map) { return userFeign.getUserInfo(map); }

    @RequestMapping("/updateUserName")
    public Message updateUserName(@RequestBody Map<String, Object> map){ return userFeign.updateUserName(map); }

    @RequestMapping("/updatePassWord")
    public Message updatePassWord(@RequestBody Map<String, Object> map){ return userFeign.updatePassWord(map); }

    @RequestMapping("/updatePhone")
    public Message updatePhone(@RequestBody Map<String, Object> map) { return userFeign.updatePhone(map); }

    @RequestMapping("/checkEmailExist")
    public Boolean checkEmailExist(@RequestBody Map<String, Object> map) {
        return userFeign.checkEmailExist(map);
    }

    @RequestMapping("/updateEmail")
    public Message updateEmail(@RequestBody Map<String, Object> map) {
        return userFeign.updateEmail(map);
    }
}
