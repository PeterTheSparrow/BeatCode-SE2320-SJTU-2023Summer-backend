package team.beatcode.consumer.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @RequestMapping("/ranks")
    List<User_record> getRanks();

    @RequestMapping("/checkEmailExist")
    Boolean checkEmailExist(@RequestBody Map<String, Object> data);
}

