package team.beatcode.consumer.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import sjtu.reins.web.utils.Message;

import java.util.Map;
@FeignClient(value="question-bank",configuration =
        {FeignClientsConfiguration.class})
public interface ProblemSetFeign {

    @RequestMapping ("/GetProblemList")
    Map<String, Object> getProblemList(@RequestBody Map<String, Object> map);

    @RequestMapping ("/GetProblemDetail")
    Map<String, Object> getProblemDetail(@RequestBody int pid);

    /*
    * 管理员进行题目的增加、修改
    * */
    @RequestMapping ("/UpdateProblem")
    Message updateProblem(@RequestBody Map<String, Object> map);

    @RequestMapping ("/UpdateTestcase")
    Message updateTestcase(@RequestParam("compressed") MultipartFile file,
                           @RequestParam("problemId") int pid);
}
