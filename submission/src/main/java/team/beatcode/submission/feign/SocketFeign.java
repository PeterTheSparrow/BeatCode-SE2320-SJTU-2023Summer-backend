package team.beatcode.submission.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value="consumer",configuration =
        {FeignClientsConfiguration.class})
public interface SocketFeign {
    @RequestMapping("/socket/inform")
    void judgeFinished(@RequestParam Integer uid, @RequestParam String sid);
}
