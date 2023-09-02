package team.beatcode.consumer.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import team.beatcode.consumer.entity.Submission;

import java.util.Map;

@FeignClient(value="submission",configuration =
        {FeignClientsConfiguration.class})
public interface SubmitFeign {
    @RequestMapping("Submit")
    String Submit(@RequestBody Map<String,Object> data);
    @RequestMapping("GetFullSubmission")
    Map<String,Object> GetFullSubmission(@RequestBody Map<String,Object> data);
    @RequestMapping("GetSubmissions")
    Map<String,Object> GetSubmissions(@RequestBody Map<String,String> data);
    @RequestMapping("WindUp")
    void WindUp(@RequestParam("sid") String sid);
}
