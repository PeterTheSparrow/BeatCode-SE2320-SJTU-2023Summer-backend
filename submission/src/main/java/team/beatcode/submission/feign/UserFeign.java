package team.beatcode.submission.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "user", configuration = FeignClientsConfiguration.class)
public interface UserFeign {
    @RequestMapping("userCompleteProblem")
    void userCompleteProblem(@RequestParam String user_id,
                                    @RequestParam String problem_id,
                                    @RequestParam String date,
                                    @RequestParam int score);
}
