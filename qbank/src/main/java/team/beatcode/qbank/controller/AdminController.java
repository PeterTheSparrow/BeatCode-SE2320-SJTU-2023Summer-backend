package team.beatcode.qbank.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;
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
@CrossOrigin("*")
public class AdminController {
    @Autowired
    // 懒惰的结果
    private ProblemRepository problemRepository;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * @param map 包含的参数：
     *            problemId: 题目的id
     *            title: 题目的标题
     *            detail: 题目的描述
     *            difficulty: 题目的难度
     *            objectArray: 题目的标签，是一个数组，每个元素是一个对象，包含以下参数：
     *            name: 标签的名字
     *            description: 标签的描述
     *            color: 标签的颜色
     *            <p>
     *            如果problemId不存在，会创建一个新的题目
     *            如果problemId存在，会更新这个题目
     *            <p>
     *
     * 关于缓存：
     * 由于这里可能涉及到对于题目的更新，所以需要清除缓存。
     * @CacheEvict 缓存清除
     *      key：指定要清除的数据
     *      allEntries = true：指定清除这个缓存中所有的数据
     *      beforeInvocation = false：缓存的清除是否在方法之前执行
     *      默认代表缓存清除操作是在方法执行之后执行;如果出现异常缓存就不会清除
     *
     *      beforeInvocation = true：
     *      代表清除缓存操作是在方法运行之前执行，无论方法是否出现异常，缓存都清除
     *
     *            value = "problem"：清除problem的缓存
     *            key = "'problemid' + #problemId" ：缓存的key是problemid+题目id
     * 清除缓存的注释：
     *            @CacheEvict(value = "problem", key = "'problemid' + #problemId")
     * */
    @RequestMapping("UpdateProblem")
    @CacheEvict(value = "problem", key = "'problemid' + #map['problemId']",beforeInvocation = true)
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

            // 清空problem的Tags
            problem.getTags().clear();

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
            @RequestPart("compressed") MultipartFile file,
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
