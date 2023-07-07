package team.beatcode.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.beatcode.entity.Submission;
import team.beatcode.service.SubmissionService;

import java.util.Map;

@RestController
public class SubmitController {
    @Autowired
    SubmissionService submissionService;
    @RequestMapping("submit")
    public String Submit(@RequestBody Map<String,Object> data)
    {
        Submission submission=new Submission();
        String lang=data.get("language").toString();
        String code=data.get("code").toString();
        submission.setSubmission_language(lang);
        submission.setSubmission_code(code);
        submission.build_conf();
        //TODO set user_name and user_id
        //TODO set problem_name and problem_id
        submissionService.saveSubmission(submission);
        return submission.get_id();
    }
}
