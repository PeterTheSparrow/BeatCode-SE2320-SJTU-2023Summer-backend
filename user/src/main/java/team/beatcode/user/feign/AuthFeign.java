package team.beatcode.user.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;


@Component
@FeignClient(value="auth",configuration =
        {FeignClientsConfiguration.class})
public interface AuthFeign {
    @RequestMapping("/checkCode")
    Integer checkCode(@RequestBody Map<String, Object> data);
}
