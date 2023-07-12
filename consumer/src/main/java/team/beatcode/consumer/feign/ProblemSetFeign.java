package team.beatcode.consumer.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import team.beatcode.qbank.entity.ProblemReturn;

import java.util.List;
import java.util.Map;
@FeignClient(value="question-bank",configuration =
        {FeignClientsConfiguration.class})
public interface ProblemSetFeign {

    @RequestMapping ("/GetProblemList")
    List<ProblemReturn> getProblemList(@RequestBody Map<String, Object> map);
}
