package team.beatcode.consumer.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;
@FeignClient(value="question-bank",configuration =
        {FeignClientsConfiguration.class})
public interface ProblemSetFeign {

    @RequestMapping ("/GetProblemList")
    Map<String, Object> getProblemList(@RequestBody Map<String, Object> map);

    @RequestMapping ("/GetProblemDetail")
    Map<String, Object> getProblemDetail(@RequestBody int pid);
}
