package team.beatcode.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sjtu.reins.web.utils.Message;
import team.beatcode.consumer.feign.ProblemSetFeign;

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
    public Map<String, Object> getProblemList(@RequestBody Map<String, Object> map) {
        return problemSetFeign.getProblemList(map);
    }

    @RequestMapping("/GetProblemDetail")
    public Map<String, Object> getProblemDetail(@RequestBody int pid) {
        return problemSetFeign.getProblemDetail(pid);
    }

    /**
     * 更新/添加题目
     * @param map 题目的json，格式如下：
     *            {
     *            "problemId": 1,
     *            "title": "题目标题",
     *            "detail": "题目描述",
     *            "difficulty": "难度",
     *            "objectArray": [
     *            {
     *            "name": "标签名",
     *            "description": "标签描述",
     *            "color": "标签颜色"
     *            }
     *            ]
     *            }
     * @return message
     * */
    @RequestMapping("/UpdateProblem")
    public Message updateProblem(@RequestBody Map<String, Object> map) {
        return problemSetFeign.updateProblem(map);
    }

    /**
     * 上传测试集压缩文件，带有漏洞百出的锁机制。请提交表单。
     * @param file 压缩文件本身，注意压缩文件的root就是其内容
     * @param pid 题目的id，必须是已经存在的题目
     * @return 如果文件格式不当，会在data输出报错log
     */
    @RequestMapping("/UpdateTestcase")
    public Message updateTestcase(@RequestParam("compressed") MultipartFile file,
                                  @RequestParam("problemId") int pid) {
        return problemSetFeign.updateTestcase(file, pid);
    }
}
