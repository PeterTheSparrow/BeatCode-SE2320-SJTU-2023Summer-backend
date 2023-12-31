package team.beatcode.qbank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sjtu.reins.web.utils.Message;
import team.beatcode.qbank.entity.ProblemReturn;
import team.beatcode.qbank.entity.UserCondition;
import team.beatcode.qbank.service.ConditionService;
import team.beatcode.qbank.service.ProblemService;
import team.beatcode.qbank.utils.Macros;
import team.beatcode.qbank.utils.msg.MessageEnum;
import team.beatcode.qbank.utils.msg.MessageException;

import java.util.Map;

@RestController
public class ProblemController {

    ProblemService problemService;
    @Autowired
    ConditionService conditionService;

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
            String user_id= (String) map.get(Macros.PARAM_USER_ID);
            if (pageIndex == null || pageSize == null ||
            titleContains == null || hardLevel == null){
                // 打印日志
                System.out.println("GetProblemList: 缺少参数");
                System.out.println("pageIndex: " + pageIndex);
                System.out.println("pageSize: " + pageSize);
                System.out.println("titleContains: " + titleContains);
                System.out.println("hardLevel: " + hardLevel);
                return new Message(MessageEnum.PARAM_FAIL);
            }

//            System.out.println("breakpoint1");

            if (pageIndex <= 0)
                return new Message(MessageEnum.SEARCH_PAGE_NEGATIVE);
            if (pageSize <= 1)
                return new Message(MessageEnum.SEARCH_PAGE_MALICE);

//            System.out.println("breakpoint2");

            //get user-problem condition
            UserCondition userCondition=conditionService.GetUserCondition(user_id);
//            System.out.println("breakpoint3");
            String user_problem_condition=userCondition.getProblemCondition();
//            System.out.println("breakpoint4");

            ProblemReturn.Paged result =
                    problemService.getProblemListEx(
                            titleContains, hardLevel, pageIndex - 1, pageSize, user_problem_condition);
//            System.out.println("breakpoint5");
            return new Message(MessageEnum.SUCCESS, result);
        }
        catch (NullPointerException e) {
            // 缺少参数（null可能无法被某些类型转换）
            // 打印日志
            System.out.println("GetProblemList: 缺少参数");
            System.out.println("map: " + map);
            System.out.println("pageIndex: " + map.get(Macros.PARAM_PAGE));
            System.out.println("pageSize: " + map.get(Macros.PARAM_PAGE_SIZE));
            System.out.println("titleContains: " + map.get(Macros.PARAM_TITLE_KEY));
            System.out.println("hardLevel: " + map.get(Macros.PARAM_HARD_LEVEL));

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

    @RequestMapping("/GetProblemVersion")
    public Integer getVersion(@RequestBody Integer pid) {
        return problemService.getProblemVersion(pid);
    }
}
