package team.beatcode.submission.async;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import team.beatcode.submission.entity.Submission;
import team.beatcode.submission.feign.JudgeFeign;

@Service
public class AsyncJudge {
    @Autowired
    JudgeFeign judgeFeign;

    @Async
    public void asyncJudge(Submission submission) {
        judgeFeign.Judge(submission);
    }
}
