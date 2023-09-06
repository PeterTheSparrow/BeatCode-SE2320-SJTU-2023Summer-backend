package team.beatcode.qbank.serviceImp;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import team.beatcode.qbank.dao.ProblemDao;
import team.beatcode.qbank.entity.Problem;
import team.beatcode.qbank.entity.ProblemReturn;
import team.beatcode.qbank.service.ProblemService;
import team.beatcode.qbank.utils.Macros;
import team.beatcode.qbank.utils.msg.MessageEnum;
import team.beatcode.qbank.utils.msg.MessageException;

import java.util.List;

@Service
public class ProblemServiceImp implements ProblemService {
    ProblemDao problemDao;

    public ProblemServiceImp(ProblemDao problemDao) {
        this.problemDao = problemDao;
    }

    @Override
    public ProblemReturn.Paged getProblemListEx(String titleContains,
                                                String difficulty,
                                                Integer pageIndex,
                                                Integer pageSize,
                                                String problem_condition)
            throws MessageException {
        if (Macros.correctHardLevel(difficulty)) {
            Page<Problem> problems = problemDao.findByAll(titleContains,
                    difficulty, pageIndex, pageSize);
            if (problems == null)
                throw new MessageException(MessageEnum.SEARCH_PAGE_FAULT);

            return new ProblemReturn.Paged(
                    problems.stream().map(p -> new ProblemReturn(p, problem_condition)).toList(),
                    problems.getTotalPages());
        }
        else
            throw new MessageException(MessageEnum.SEARCH_DIFFICULTY_UNKNOWN);
    }

    /**
     * 试验：加入缓存
     *
     * 测试加缓存和不加缓存的查询时间；
     * 缓存的value是problem，key是"problemid"+problemId
     *
     * 注解：
     * @Cacheable(value = "problem", key = "'problemid' + #problemId")
     * value: 缓存的名字
     * key: 缓存的键
     * */
//    @Cacheable(value = "problem", key = "'problemid' + #problemId")
    @Override
    public ProblemReturn.Detail getProblemDetail(Integer problemId) {
        Problem p = problemDao.findProblemById(problemId);

        if (p == null)
            return null;
        else {
            return new ProblemReturn.Detail(p);
        }
    }

    @Override
    public int getProblemVersion(Integer problemId) {
        return problemDao.findVersionById(problemId);
    }

    @Override
    public Page<Problem> getUserProblem(List<Integer> ProblemIds, Integer pageIndex, Integer pageSize) {
        return problemDao.findByIds(ProblemIds, pageIndex, pageSize);
    }

}
