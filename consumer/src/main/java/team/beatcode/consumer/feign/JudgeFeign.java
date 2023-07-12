package team.beatcode.consumer.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value="judge",configuration =
        {FeignClientsConfiguration.class})
public interface JudgeFeign {
    @RequestMapping("Judge")
    void Judge(@RequestParam("sid") String sid);
}
