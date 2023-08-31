package team.beatcode.judge.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value="question-bank",configuration =
        {FeignClientsConfiguration.class})
public interface QBankFeign {
    @RequestMapping("/GetProblemVersion")
    Integer getVersion(@RequestBody Integer pid);
}
