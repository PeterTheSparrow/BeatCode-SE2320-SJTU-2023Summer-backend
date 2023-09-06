package team.beatcode.consumer.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import sjtu.reins.web.utils.Message;
import team.beatcode.user.entity.Person_info;
import team.beatcode.user.entity.User;
import team.beatcode.user.entity.User_record;

import java.util.List;
import java.util.Map;

@FeignClient(value = "user", configuration = FeignClientsConfiguration.class)
public interface UserFeign {
    @RequestMapping("/user")
    User getUser(@RequestBody Map<String, Object> map);

    @RequestMapping("/register")
    void register(@RequestBody Map<String, Object> data);

    @RequestMapping("/getRanking")
    Map<String,Object> getRanking(@RequestBody Map<String,Object> data);

    @RequestMapping("/checkUserExist")
    Boolean checkUserExist(@RequestBody Map<String, Object> data);

    @RequestMapping("/getUserInfo")
    Person_info getUserInfo(@RequestBody Map<String, Object> data);

    @RequestMapping("/updateUserName")
    Message updateUserName(@RequestBody Map<String, Object> data);

    @RequestMapping("/updatePassWord")
    Message updatePassWord(@RequestBody Map<String, Object> data);

    @RequestMapping("/updatePhone")
    Message updatePhone(@RequestBody Map<String, Object> data);

    @RequestMapping("/checkEmailExist")
    Boolean checkEmailExist(@RequestBody Map<String, Object> data);

    @RequestMapping("/updateEmail")
    Message updateEmail(@RequestBody Map<String, Object> data);

    @RequestMapping("/getProblemList")
    Message getProblemList(@RequestBody Map<String, Object> map);
}

