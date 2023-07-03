package team.beatcode.judge.controller;

import com.netflix.appinfo.EurekaInstanceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JudgeController {

    @Value("${server.port}")
    private int serverPort = 0;

    @RequestMapping("judge")
    public String judgeBySid(@RequestBody String sid_s) {
        return sid_s;
    }
}
