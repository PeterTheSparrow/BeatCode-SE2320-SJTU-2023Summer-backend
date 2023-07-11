package team.beatcode.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import team.beatcode.consumer.entity.Submission;
import team.beatcode.consumer.feign.SubmitFeign;
import team.beatcode.consumer.interceptors.RequireLogin;

import java.util.Map;

@CrossOrigin(origins = "http://10.180.33.155:3000")
@RestController
public class SubmitController {
    @Autowired
    SubmitFeign submitFeign;
    @RequestMapping("Submit")
//    @RequireLogin(type = RequireLogin.Type.USER)
    public String Submit(@RequestBody Map<String,Object> data)
    {
        return submitFeign.Submit(data);
    }

    @RequestMapping("GetFullSubmission")
//    @RequireLogin(type = RequireLogin.Type.USER)
    public Submission GetFullSubmission(@RequestBody Map<String,Object> data)
    {
        return submitFeign.GetFullSubmission(data);
    }
    @RequestMapping("GetSubmissions")
//    @RequireLogin(type = RequireLogin.Type.USER)
    public Page<Submission> GetSubmissions(@RequestBody Map<String,String> data)
    {
        System.out.println("received page "+data.get("page"));
        return submitFeign.GetSubmissions(data);
    }
}
