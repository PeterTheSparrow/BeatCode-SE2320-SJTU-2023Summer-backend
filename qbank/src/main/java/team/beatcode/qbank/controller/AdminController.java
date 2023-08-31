package team.beatcode.qbank.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sjtu.reins.web.utils.Message;
import team.beatcode.qbank.entity.Problem;
import team.beatcode.qbank.repository.ProblemRepository;
import team.beatcode.qbank.utils.Testcase7zTools;
import team.beatcode.qbank.utils.msg.MessageEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class AdminController {
    @Autowired
    // 懒惰的结果
    private ProblemRepository problemRepository;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 更新题干部分。题干与测试集是分开的，必须先上传题干才能上传测试集<br>
     * config是上传测试集后自动生成的，与此处无关
     * @param map "problemId": 题号，如果不存在则新建<br>
     *            "title": 标题<br>
     *            "detail": 题干的Markdown<br>
     *            "difficulty": 难度，未检查合法性<br>
     *            "tags": 标签数组<br>
     *            &emsp;&emsp;"name": 标签类型<br>
     *            &emsp;&emsp;"description": 说明<br>
     *            &emsp;&emsp;"color": 颜色，未检查合法性
     * @return Message，缺少参数时报错
     */
    @RequestMapping("UpdateProblem")
    public Message updateProblem(@RequestBody Map<String, Object> map) {
        try {
            Integer pid = (Integer) map.get("problemId");
            Problem problem = problemRepository.findProblemByTitleId(pid);
            if (problem == null) {
                problem = new Problem();

                problem.setTitle(new Problem.Title());
                problem.setConfig(new Problem.Config());
                problem.setVersion(0);
                problem.setLocked(false);
            }

            problem.getTitle().setId(pid);

            String title = (String) map.get("title");
            if (title == null)
                return new Message(MessageEnum.ADMIN_PROBLEM_LACK_PARAM);
            problem.getTitle().setName(title);

            String detail = (String) map.get("detail");
            if (detail == null)
                return new Message(MessageEnum.ADMIN_PROBLEM_LACK_PARAM);
            problem.setDetail(detail);

            String difficulty = (String) map.get("difficulty");
            if (difficulty == null)
                return new Message(MessageEnum.ADMIN_PROBLEM_LACK_PARAM);
            problem.setDifficulty(difficulty);

            problem.setTags(new ArrayList<>());
            List<Map<String, Object>> tags = objectMapper.convertValue(
                    map.get("tags"), new TypeReference<>() {});
            for (Map<String, Object> tag : tags) {
                Problem.Tag pt = new Problem.Tag();

                String name = (String) tag.get("name");
                if (name == null)
                    return new Message(MessageEnum.ADMIN_PROBLEM_LACK_PARAM);
                pt.setTag(name);

                String description = (String) tag.get("description");
                if (description == null)
                    return new Message(MessageEnum.ADMIN_PROBLEM_LACK_PARAM);
                pt.setCaption(description);

                String color = (String) tag.get("color");
                if (color == null)
                    return new Message(MessageEnum.ADMIN_PROBLEM_LACK_PARAM);
                pt.setColor(color);

                problem.getTags().add(pt);
            }

            problemRepository.save(problem);
            return new Message(MessageEnum.SUCCESS);

        } catch (NullPointerException | ClassCastException | IllegalArgumentException e) {
            return new Message(MessageEnum.ADMIN_PROBLEM_LACK_PARAM);
        }
    }

    /**
     * 上传测试集压缩文件，带有漏洞百出的锁机制。请提交表单。
     * @param file 压缩文件本身，注意压缩文件的root就是其内容
     * @param pid 题目的id，必须是已经存在的题目
     * @return 如果文件格式不当，会在data输出报错log
     */
    @RequestMapping("UpdateTestCase")
    public Message updateTestCase(
            @RequestParam("compressed") MultipartFile file,
            @RequestParam("problemId") int pid) {
        // lock it
        Problem problem = problemRepository.findProblemByTitleId(pid);
        if (problem == null)
            return new Message(MessageEnum.ADMIN_PROBLEM_NOT_FOUND);
        // 兼容之前的数据...不知道怎么拿Compass批量改数据
        if (problem.getLocked() != null && problem.getLocked())
            return new Message(MessageEnum.ADMIN_PROBLEM_LOCKED);

        // problem.setLocked(true);
        problemRepository.save(problem);

        // generate task
        String task = String.format("%s-%6x",
                UUID.randomUUID(), System.currentTimeMillis() & 0xffffff)
                // 路径名带空格是危险的行为
                .replace(' ', 's');
        if (!Testcase7zTools.downloadToTmp(task, file) ||
            !Testcase7zTools.unzipToTmp(task)) {
            Testcase7zTools.cleanse(task);
            return new Message(MessageEnum.SYSTEM_ERROR);
        }

        // check
        Testcase7zTools.AnalyzeReport report = Testcase7zTools.analyze(task);
        if (report == null) {
            Testcase7zTools.cleanse(task);
            return new Message(MessageEnum.SYSTEM_ERROR);
        } else if (!report.log.isEmpty()) {
            Testcase7zTools.cleanse(task);
            return new Message(MessageEnum.ADMIN_PROBLEM_PACKAGE_ERROR,
                               report.log);
        } else {
            // update & unlock
            problem.setLocked(false);
            problem.setConfig(report.config);
            problem.setVersion(problem.getVersion() + 1);
            problemRepository.save(problem);

            Testcase7zTools.update(task, pid);
            Testcase7zTools.cleanse(task);
            return new Message(MessageEnum.SUCCESS);
        }
    }
}
