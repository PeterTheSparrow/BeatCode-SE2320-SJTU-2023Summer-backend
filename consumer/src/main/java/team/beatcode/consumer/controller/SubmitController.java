package team.beatcode.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.beatcode.consumer.feign.JudgeFeign;
import team.beatcode.consumer.feign.SubmitFeign;
import team.beatcode.consumer.interceptors.RequireLogin;
import team.beatcode.consumer.utils.context.UserContext;
import team.beatcode.consumer.utils.context.UserContextHolder;

import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
public class SubmitController {
    @Autowired
    SubmitFeign submitFeign;
    @Autowired
    JudgeFeign judgeFeign;
    @RequestMapping("Submit")
    @RequireLogin(type = RequireLogin.Type.USER)
    public String Submit(@RequestBody Map<String,Object> data)
    {
        UserContext userContext= UserContextHolder.getUserAccount();
        data.put("user_id",userContext.getUser_id());
        data.put("user_name",userContext.getUser_name());
        String sid=submitFeign.Submit(data);
System.out.println("created submission id "+sid);
        return sid;
    }

    @RequestMapping("GetFullSubmission")
    @RequireLogin(type = RequireLogin.Type.USER)
    public Map<String,Object> GetFullSubmission(@RequestBody Map<String,Object> data)
    {
        return submitFeign.GetFullSubmission(data);
    }
    @RequestMapping("GetSubmissions")
    @RequireLogin(type = RequireLogin.Type.USER)
    public Map<String,Object> GetSubmissions(@RequestBody Map<String,String> data)
    {
        return submitFeign.GetSubmissions(data);
    }
    @RequestMapping("GetProblemSubmissions")
    @RequireLogin(type = RequireLogin.Type.USER)
    public Map<String,Object> GetProblemSubmissions(@RequestBody Map<String,String> data)
    {
        return submitFeign.GetProblemSubmissions(data);
    }
}
