package team.beatcode.judge.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import team.beatcode.judge.utils.TestCaseGetter;

@RestController
public class JudgeTestController {
    @Autowired
    private RestTemplate restTemplate;

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
