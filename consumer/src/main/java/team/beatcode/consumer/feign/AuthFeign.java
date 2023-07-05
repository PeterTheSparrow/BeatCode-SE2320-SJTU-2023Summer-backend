package team.beatcode.consumer.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import sjtu.reins.web.utils.Message;

import java.util.Map;

@FeignClient(value="auth",configuration =
        {AuthFeignConfig.class})
public interface AuthFeign {
    @RequestMapping("/CheckUser")
    String checkUser();
    @RequestMapping("/CheckAdmin")
    String checkAdmin();

    @RequestMapping("/Login")
    Message login(@RequestBody Map<String, Object> map);
    @RequestMapping("/Logout")
    Message logout();
    @RequestMapping("/Register")
    Message register(@RequestBody Map<String, Object> map);
}