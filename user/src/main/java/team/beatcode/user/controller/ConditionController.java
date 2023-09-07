package team.beatcode.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.beatcode.user.entity.UserCondition;
import team.beatcode.user.service.ConditionService;

import java.util.Map;

@RestController
public class ConditionController {
    @Autowired
    ConditionService conditionService;
    @RequestMapping("getRanking")
    public Page<UserCondition> getRanking(@RequestBody Map<String,String> data)
    {
        return conditionService.getPaginatedRanking(data);
    }

    @RequestMapping("getUserConditionById")
    public UserCondition getUserConditionById(@RequestBody String user_id) {
        return conditionService.GetUserCondition(user_id);
    }

    @RequestMapping("saveUserCondition")
    public void saveUserCondition(@RequestBody UserCondition condition) {
        conditionService.saveUserCondition(condition);
    }
}
