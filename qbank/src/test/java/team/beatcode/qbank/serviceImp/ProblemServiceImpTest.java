package team.beatcode.qbank.serviceImp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import team.beatcode.qbank.dao.ProblemDao;
import team.beatcode.qbank.entity.Problem;
import team.beatcode.qbank.entity.ProblemReturn;
import team.beatcode.qbank.utils.msg.MessageException;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class ProblemServiceImpTest {
    @Mock
    private ProblemDao problemDao;

    private ProblemServiceImp problemService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        problemService = new ProblemServiceImp(problemDao);
    }

    @Test
    public void testGetProblemListEx_有效输入_返回分页结果() throws MessageException {
        // 模拟 problemDao.findByAll() 方法返回一个模拟的 Page 对象
        Page<Problem> mockedPage = createMockedPage();
        when(problemDao.findByAll(anyString(), anyString(), anyInt(), anyInt())).thenReturn(mockedPage);

        // 调用要测试的方法
        ProblemReturn.Paged result = problemService.getProblemListEx("标题", "简单", 1, 10);

        // 断言
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.getTotal());
        Assertions.assertEquals(2, result.getPage().size());
    }

    @Test
    public void testGetProblemListEx_无效难度_抛出MessageException() {
        // 调用要测试的方法，并断言它会抛出 MessageException 异常
        Assertions.assertThrows(MessageException.class, () -> problemService.getProblemListEx("标题", "无效", 1, 10));
    }

    @Test
    public void testGetProblemDetail_有效问题ID_返回Detail对象() {
        // 模拟 problemDao.findProblemById() 方法返回一个模拟的 Problem 对象
        Problem mockedProblem = createMockedProblem();
        when(problemDao.findProblemById(anyInt())).thenReturn(mockedProblem);

        // 调用要测试的方法
        ProblemReturn.Detail result = problemService.getProblemDetail(1);

        // 断言
        Assertions.assertNotNull(result);
        Assertions.assertEquals(mockedProblem.getDetail(), result.getDetail());
    }

    @Test
    public void testGetProblemDetail_无效问题ID_返回Null() {
        // 模拟 problemDao.findProblemById() 方法返回 null
        when(problemDao.findProblemById(anyInt())).thenReturn(null);

        // 调用要测试的方法
        ProblemReturn.Detail result = problemService.getProblemDetail(1);

        // 断言
        Assertions.assertNull(result);
    }

    @Test
    public void testGetProblemListEx_问题列表为空_抛出MessageException() throws MessageException{
        // 模拟 problemDao.findByAll() 方法返回 null
        when(problemDao.findByAll(anyString(), anyString(), anyInt(), anyInt())).thenReturn(null);

        // 调用要测试的方法，并断言会抛出 MessageException 异常
        Assertions.assertThrows(MessageException.class, () ->
                problemService.getProblemListEx("标题", "简单", 1, 10));
    }

    // 辅助方法，用于创建一个模拟的 Page<Problem> 对象
    private Page<Problem> createMockedPage() {
        List<Problem> problems = new ArrayList<>();
        problems.add(createMockedProblem());
        problems.add(createMockedProblem());
        return new PageImpl<>(problems, PageRequest.of(1, 10), 2);
    }

    // 辅助方法，用于创建一个模拟的 Problem 对象
    private Problem createMockedProblem() {
        Problem problem = new Problem();
        problem.setId("1");
        Problem.Title title = new Problem.Title();
        title.setName("问题标题");
        title.setId(1);
        problem.setTitle(title);
        problem.setDetail("问题详细信息");
        problem.setDifficulty("简单");

        Problem.Config config = new Problem.Config();
        config.setType("类型");
        config.setTLimit(1000);
        config.setMLimit(256);
        problem.setConfig(config);

        return problem;
    }
}











