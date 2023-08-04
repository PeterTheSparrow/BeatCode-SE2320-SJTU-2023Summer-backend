package team.beatcode.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.beatcode.consumer.feign.ProblemSetFeign;
import team.beatcode.consumer.interceptors.RequireLogin;
import team.beatcode.consumer.utils.context.UserContext;
import team.beatcode.consumer.utils.context.UserContextHolder;

import java.util.Map;

/*
* 这里不加crossOrigin，浏览器是无法访问的，因为跨域了
* 但是如果单纯设置3000端口，其他端口都被block了，所以这里设置为*
* */
@RestController
@CrossOrigin(origins = "*")
public class ProblemSetController {
    @Autowired
    ProblemSetFeign problemSetFeign;

    @RequestMapping("/GetProblemList")
    @RequireLogin(type = RequireLogin.Type.USER)
    public Map<String, Object> getProblemList(@RequestBody Map<String, Object> map) {
        UserContext userContext= UserContextHolder.getUserAccount();
        map.put("user_id",userContext.getUser_id().toString());
        return problemSetFeign.getProblemList(map);
    }

    @RequestMapping("/GetProblemDetail")
    public Map<String, Object> getProblemDetail(@RequestBody int pid) {
        return problemSetFeign.getProblemDetail(pid);
    }
}
