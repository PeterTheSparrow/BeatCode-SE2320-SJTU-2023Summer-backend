package team.beatcode.consumer.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import sjtu.reins.web.utils.Message;

import java.util.Map;

@FeignClient(value = "user", configuration = UserFeignConfig.class)
public interface UserFeign {
    @RequestMapping("/user")
    Message getUser(@RequestBody Map<String, Object> map);

    @RequestMapping("/register")
    Message register(@RequestBody Map<String, Object> data);

    @RequestMapping("/ranks")
    Message getRanks();
}

