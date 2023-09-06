package team.beatcode.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.beatcode.entity.Submission;
import team.beatcode.entity.UserCondition;
import team.beatcode.feign.SocketFeign;
import team.beatcode.service.ConditionService;
import team.beatcode.service.SubmissionService;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class EndingController {
    @Autowired
    SubmissionService submissionService;
    @Autowired
    ConditionService conditionService;
    @Autowired
    SocketFeign socketFeign;
    @RequestMapping("WindUp")
    public void WindUp(@RequestBody Submission submission)
    {
        submissionService.saveSubmission(submission);

        String userId=submission.getUserId();
        String problemId=submission.getProblemId();
        String problemScore=submission.getResult_score();
        UserCondition userCondition=conditionService.GetUserCondition(userId);
        String condition=userCondition.getProblemCondition();
        int count=userCondition.getACount();


        String regex="<"+problemId+">([\\s\\S]*)</"+problemId+">";
        String originCondition="";
        Pattern pattern= Pattern.compile(regex);
        Matcher matcher= pattern.matcher(condition);
        if(matcher.find())
        {
            originCondition=matcher.group(1);
        }

        if(Objects.equals(originCondition, ""))
        {
            condition=condition+"\n<"+problemId+">"+problemScore+"</"+problemId+">";
        }
        else if(Integer.parseInt(originCondition) < Integer.parseInt(problemScore))
        {
            condition=condition.replace("<"+problemId+">"+originCondition+"</"+problemId+">",
                    "<"+problemId+">"+problemScore+"</"+problemId+">");
        }

        if(Integer.parseInt(problemScore)==100 && (originCondition.equals("") || Integer.parseInt(originCondition)<100))
            count++;

        userCondition.setProblemCondition(condition);
        userCondition.setACount(count);
        conditionService.SaveUserCondition(userCondition);
        // 发送WebSocket到用户
        socketFeign.judgeFinished(
                Integer.parseInt(submission.getUserId()),
                "Judge Finished");
    }
}
