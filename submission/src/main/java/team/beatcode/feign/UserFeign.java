package team.beatcode.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "user", configuration = FeignClientsConfiguration.class)
public interface UserFeign {
    void userCompleteProblem(@RequestParam String user_id,
                                    @RequestParam String problem_id,
                                    @RequestParam String date,
                                    @RequestParam int score);
}
