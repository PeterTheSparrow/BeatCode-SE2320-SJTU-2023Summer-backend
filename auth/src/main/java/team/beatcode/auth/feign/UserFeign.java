package team.beatcode.auth.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@FeignClient(value="user",configuration =
        {FeignClientsConfiguration.class})
public interface UserFeign {
    @RequestMapping("update")
    void update(@RequestBody Map<String, Object> map);
}
