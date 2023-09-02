package team.beatcode.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.beatcode.user.entity.UserCondition;
import team.beatcode.user.service.ConditionService;

import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class ConditionController {
    @Autowired
    ConditionService conditionService;
    @RequestMapping("getRanking")
    public Page<UserCondition> getRanking(@RequestBody Map<String,String> data)
    {
        return conditionService.getPaginatedRanking(data);
    }
}
