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

    @RequestMapping("UpdateProblem")
    public Message updateProblem(@RequestBody Map<String, Object> map) {
        try {
            Integer pid = (Integer) map.get("problemId");
            Problem problem = problemRepository.findProblemByTitleId(pid);
            if (problem == null) {
                problem = new Problem();

                problem.setTitle(new Problem.Title());
                problem.setConfig(new Problem.Config());
                problem.setTags(new ArrayList<>());
                problem.setVersion(0);
                problem.setLocked(false);
            }

            problem.getTitle().setId(pid);
            problem.getTitle().setName((String) map.get("title"));

            problem.setDetail((String) map.get("detail"));
            problem.setDifficulty((String) map.get("difficulty"));

            List<Map<String, Object>> tags = objectMapper.convertValue(
                    map.get("objectArray"), new TypeReference<>() {});
            for (Map<String, Object> tag : tags) {
                Problem.Tag pt = new Problem.Tag();
                pt.setTag((String) tag.get("name"));
                pt.setCaption((String) tag.get("description"));
                pt.setColor((String) tag.get("color"));
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
        if (problem.getLocked())
            return new Message(MessageEnum.ADMIN_PROBLEM_LOCKED);

        problem.setLocked(true);
        problemRepository.save(problem);

        // generate task
        String task = String.format("%s-%16x",
                UUID.randomUUID(), System.currentTimeMillis() & 0xffffff);
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
