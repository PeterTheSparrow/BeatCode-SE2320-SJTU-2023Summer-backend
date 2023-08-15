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
    Message checkUser();
    @RequestMapping("/CheckAdmin")
    Message checkAdmin();

    @RequestMapping("/Login")
    Message login(@RequestBody Map<String, Object> map);
    @RequestMapping("/Logout")
    Message logout();
    @RequestMapping("/Register")
    Message register(@RequestBody Map<String, Object> map);

    @RequestMapping("/createCode")
    String create(@RequestBody Map<String, Object> data);

    @RequestMapping("/checkCode")
    Integer check(@RequestBody Map<String, Object> data);
}
