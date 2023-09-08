package team.beatcode.submission.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.beatcode.submission.entity.Submission;
import team.beatcode.submission.feign.SocketFeign;
import team.beatcode.submission.feign.UserFeign;
import team.beatcode.submission.service.SubmissionService;

@RestController
public class EndingController {
    @Autowired
    SubmissionService submissionService;
    @Autowired
    UserFeign userFeign;
    @Autowired
    SocketFeign socketFeign;
    @RequestMapping("WindUp")
    public void WindUp(@RequestBody Submission submission)
    {
        System.out.println("saved submission in windup1:");
        System.out.println(submission);
        submissionService.saveSubmission(submission);
        System.out.println("saved submission in windup2:");
        System.out.println(submission);

        // 更新UserCondition统计表
        userFeign.userCompleteProblem(
                submission.getUserId(),
                submission.getProblemId(),
                submission.getSubmission_time(),
                Integer.parseInt(submission.getResult_score()));

        // 发送WebSocket到用户
        socketFeign.judgeFinished(
                Integer.parseInt(submission.getUserId()),
                submission.get_id());
    }
}
