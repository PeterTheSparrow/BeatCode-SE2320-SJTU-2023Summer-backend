package team.beatcode.qbank.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sjtu.reins.web.utils.Message;
import team.beatcode.qbank.entity.ProblemReturn;
import team.beatcode.qbank.service.ProblemService;
import team.beatcode.qbank.utils.Macros;
import team.beatcode.qbank.utils.msg.MessageEnum;
import team.beatcode.qbank.utils.msg.MessageException;

import java.util.Map;

@RestController
public class ProblemController {

    ProblemService problemService;

    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    /**
     * 题库主界面：接收的前端的参数，进行筛选，返回符合条件的题目
     * pageIndex: 当前页数，从1开始
     * pageSize: 每页的记录数目
     * titleContains: 标题包含此关键词。不能不填，可以是空串
     * hardLevel: 难度。不能不填，只能是空串或者Macros里规定的几个值，否则会报错
     * 填空串等于返回全部
     * */
    @RequestMapping("/GetProblemList")
    public Message getProblemList(@RequestBody Map<String, Object> map) {
        try {
            Integer pageIndex = (Integer) map.get(Macros.PARAM_PAGE);
            Integer pageSize = (Integer) map.get(Macros.PARAM_PAGE_SIZE);

            String titleContains = (String) map.get(Macros.PARAM_TITLE_KEY);
            String hardLevel = (String) map.get(Macros.PARAM_HARD_LEVEL);

            if (pageIndex == null || pageSize == null ||
            titleContains == null || hardLevel == null)
                return new Message(MessageEnum.PARAM_FAIL);

            if (pageIndex <= 0)
                return new Message(MessageEnum.SEARCH_PAGE_NEGATIVE);
            if (pageSize <= 1)
                return new Message(MessageEnum.SEARCH_PAGE_MALICE);

            ProblemReturn.Paged result =
                    problemService.getProblemListEx(
                            titleContains, hardLevel, pageIndex - 1, pageSize);

            return new Message(MessageEnum.SUCCESS, result);
        }
        catch (NullPointerException e) {
            // 缺少参数（null可能无法被某些类型转换）
            return new Message(MessageEnum.PARAM_FAIL);
        }
        catch (MessageException e) {
            return new Message(e.getE());
        }
    }

    @RequestMapping("/GetProblemDetail")
    public ProblemReturn.Detail getProblemDetail(@RequestBody Integer pid) {
        return problemService.getProblemDetail(pid);
    }
}
