package team.beatcode.consumer.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import team.beatcode.consumer.interceptors.RequireLogin;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class SubmitController {
    @PostMapping("Submit")
    @RequireLogin(type = RequireLogin.Type.USER)
    public String handleSubmission(@RequestBody Map<String,Object> sbm)
    {

        return "";
    }
}
