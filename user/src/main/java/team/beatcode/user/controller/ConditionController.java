package team.beatcode.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.beatcode.user.entity.UserCondition;
import team.beatcode.user.service.ConditionService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
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

    @RequestMapping("getUserConditionById")
    public UserCondition getUserConditionById(@RequestBody String user_id) {
        return conditionService.GetUserCondition(user_id);
    }

    @RequestMapping("saveUserCondition")
    public void saveUserCondition(@RequestBody UserCondition condition) {
        conditionService.saveUserCondition(condition);
    }

    @RequestMapping("userCompleteProblem")
    public void userCompleteProblem(@RequestParam String user_id,
                                    @RequestParam String problem_id,
                                    @RequestParam String date,
                                    @RequestParam int score) {
        // todo 搬到service并处理并发锁？
        UserCondition userCondition = conditionService.GetUserCondition(user_id);

        //modify the problem condition
        LinkedHashMap<String, Integer> pCond = userCondition.getProblemCondition();
        Integer originScore = pCond.get(problem_id);
        if (originScore == null || originScore < score) {
            pCond.put(problem_id, score);
            userCondition.setProblemCondition(pCond);
        }


        if ((originScore == null || originScore != 100) && score == 100)
            userCondition.setACount(userCondition.getACount() + 1);

        //add to activity
        LinkedHashMap<String, Integer> uAct = userCondition.getUserActivity();
        String formattedDate = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                                                    .format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        uAct.merge(formattedDate, 1, Integer::sum);
        userCondition.setUserActivity(uAct);

        conditionService.saveUserCondition(userCondition);
    }
}
