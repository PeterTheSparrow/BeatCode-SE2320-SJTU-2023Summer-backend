package team.beatcode.qbank.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import team.beatcode.qbank.entity.UserCondition;

@FeignClient(value = "user", configuration = FeignClientsConfiguration.class)
public interface UserFeign {
    @RequestMapping("getUserConditionById")
    public UserCondition getUserConditionById(@RequestBody String user_id);

}
