package team.beatcode.qbank.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import team.beatcode.qbank.entity.ProblemReturn;
import team.beatcode.qbank.service.ProblemService;
import team.beatcode.qbank.utils.Macros;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public class ProblemController {

    ProblemService problemService;

    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    private ResponseEntity<StreamingResponseBody> gt(String path) {
        if (path == null) {
            return ResponseEntity.noContent().build();
        }

        // 使用lambda规定文件的流传输方式
        StreamingResponseBody responseBody = outputStream -> {
            try (FileInputStream fileInputStream = new FileInputStream(path)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    // System.out.write(buffer, 0, bytesRead);
                    outputStream.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .body(responseBody);
    }

    /**
     * 通过流传输得到最新的test-case压缩包
     * @param pid 题目的id
     * @return 流
     */
    @RequestMapping("/GetTestCase")
    public ResponseEntity<StreamingResponseBody> getTestCase(@RequestParam int pid) {
        return gt(Macros.getTestCaseFilePath(pid));
    }

    /*
     * TODO 题库主界面：接收的前端的参数
     * 1. 页面数（传最多的记录的数目给前端，否则数据量太大）（所以是否需要当前页数？）（每页的记录数目？这个反正就一个json打包发过来）
     * 2. 排序方式（按照什么排序？）（几个条件一起json打包发过来，空的就不管了）
     * Q:前端是每次只能有一个查找条件吧？
     * 反正就是把参数传到服务层，服务层里面执行具体的查找。
     * 返回的时候可以新建一个实体ProblemForReturn，因为很多信息返回到前端主页面是没有意义的，比如题目的具体描述信息。
     * 传到前端就用List，直接spring帮忙解析成json。
     */

    /**
     * 题库主界面：接收的前端的参数，进行筛选，返回符合条件的题目
     * pageIndex: 当前页数
     * pageSize: 每页的记录数目
     * searchIndex: 根据什么来搜索
     * searchKeyWord: 搜索的关键词（用户输入的）
     * "pageIndex": 1,
     * "pageSize": 10,
     * "searchIndex": "title",
     * "searchKeyWord": "和"
     * */
    @RequestMapping("/GetProblemList")
    public List<ProblemReturn> getProblemList(@RequestBody Map<String, Object> map) {
        // 解析参数，如果缺少，就返回null
        if (map.get(Macros.PARAM_PAGE) == null ||
                map.get(Macros.PARAM_PAGE_SIZE) == null ||
                map.get(Macros.PARAM_SEARCH_TYPE) == null ||
                map.get(Macros.PARAM_KEYWORD) == null) {
            return null;
        }

        Integer pageIndex = (Integer) map.get(Macros.PARAM_PAGE);
        Integer pageSize = (Integer) map.get(Macros.PARAM_PAGE_SIZE);

        String searchIndex = (String) map.get(Macros.PARAM_SEARCH_TYPE);
        String searchKeyWord = (String) map.get(Macros.PARAM_KEYWORD);

        return problemService.getProblemList(pageIndex, pageSize, searchIndex, searchKeyWord);
    }
}
