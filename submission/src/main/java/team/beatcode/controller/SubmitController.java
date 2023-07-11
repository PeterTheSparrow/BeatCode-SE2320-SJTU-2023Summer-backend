package team.beatcode.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    @RequestMapping("Submit")
    public String Submit(@RequestBody Map<String,Object> data)
    {
        //needing params:
        //language: using language
        //code: code text
        //problem_name
        //problem_id
        Submission submission=new Submission();
        String lang=data.get("language").toString();
        String code=data.get("code").toString();
        submission.setSubmission_language(lang);
        submission.setSubmission_code(code);
        submission.build_conf();
        //TODO set user_name and user_id
        //TODO set problem_name and problem_id
        submissionService.saveSubmission(submission);
        return submission.get_id().toString();
    }
    @RequestMapping("GetFullSubmission")
    public Submission GetFullSubmission(@RequestBody Map<String,Object> data)
    {
        String sid=data.get("id").toString();
        return submissionService.getSubmission(sid);
    }
    @RequestMapping("GetSubmissions")
    public Page<Submission> GetSubmissions(@RequestBody Map<String,String>data)
    {
        //needing params:
        //sortDirection: asc or desc(default)
        //sortBy: sorted field (set time as default)
        //user_name: filtered username
        //problem_name: filtered problem name
        //problem_id: filtered problem id
        //page: present page
        //pageSize: size of per page
        return submissionService.getPaginatedSubmissions(data);
    }
}
