package team.beatcode.user.feign;

import org.springframework.data.domain.Page;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import team.beatcode.qbank.entity.Problem;

import java.util.Map;

@Component
@FeignClient(value="question-bank",configuration =
        {FeignClientsConfiguration.class})
public interface QbankFeign {

    @RequestMapping("/getUserProblem")
    Page<Problem> getUserProblem(@RequestBody Map<String, Object> map);
}
