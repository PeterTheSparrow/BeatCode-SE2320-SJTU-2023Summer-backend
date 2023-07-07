package team.beatcode.judge.controller;

import com.netflix.appinfo.EurekaInstanceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import team.beatcode.judge.utils.TestCaseGetter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
public class JudgeTestController {

    protected Logger logger = LoggerFactory.getLogger(JudgeTestController.class);

    @Autowired
    private EurekaInstanceConfig eurekaInstanceConfig;
    @Autowired
    private RestTemplate restTemplate;

    @Value("${server.port}")
    private int serverPort = 0;

    @RequestMapping("helloPORT")
    public String helloPORT() {
        this.logger.info("/hello, instanceId:{}, host:{}", eurekaInstanceConfig.getInstanceId(), eurekaInstanceConfig.getHostName(false));
        return "Hello, Spring Cloud! My port is " + String.valueOf(serverPort);
    }

    @RequestMapping("/fetch-data")
    public String fetchData() {
        ResponseEntity<Resource> response =
                restTemplate.getForEntity("http://question-bank/getDataTest", Resource.class);
        Resource resource = response.getBody();
        if (resource == null) return "ko";

        try (InputStream inputStream = resource.getInputStream();
             FileOutputStream outputStream = new FileOutputStream("D:\\Work\\to")) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "ok";
    }

    @RequestMapping("/downloadTest")
    public boolean downloadTest() {
        return TestCaseGetter.downloadTestCasesZip(restTemplate, 1, "D:\\Work\\Software-School-Projects\\BeatCode-test");
    }

    @RequestMapping("/unzipTest")
    public boolean unzipTest() {
        return TestCaseGetter.updateLocalTestCase(1,
                "D:\\Work\\Software-School-Projects\\BeatCode-test",
                "D:\\Work\\Software-School-Projects\\BeatCode-test-unzip");
    }
}
