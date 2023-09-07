package team.beatcode.judge.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import team.beatcode.judge.entity.Submission;

@FeignClient(value="submission",configuration =
        {FeignClientsConfiguration.class})
public interface SubmitFeign {
    @RequestMapping("WindUp")
    void WindUp(@RequestBody Submission submission);
}
