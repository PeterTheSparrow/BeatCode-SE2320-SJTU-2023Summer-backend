package team.beatcode.auth.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import sjtu.reins.web.utils.Message;

import java.util.Map;

@FeignClient(value="user",configuration =
        {FeignClientsConfiguration.class})
public interface UserFeign {
    @RequestMapping("register")
    void registerUser(@RequestBody Map<String, Object> map);

    @RequestMapping("checkEmailExist")
    Boolean checkEmailExist(@RequestBody Map<String, Object> map);
}

