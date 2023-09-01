package team.beatcode.judge.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.beatcode.judge.service.VersionService;

@RestController
public class JudgeTestController {
    @Autowired
    VersionService versionService;

    @RequestMapping("/Test")
    public boolean versionTest() {
        return versionService.checkVersion(54749110);
    }
}
