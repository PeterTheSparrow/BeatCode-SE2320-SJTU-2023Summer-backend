package team.beatcode.submission.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import team.beatcode.submission.entity.Submission;

@FeignClient(value="judge",configuration =
        {FeignClientsConfiguration.class})
public interface JudgeFeign {
    @RequestMapping("Judge")
    void Judge(@RequestBody Submission submission);
}
