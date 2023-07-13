package team.beatcode.qbank.daoImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import team.beatcode.qbank.entity.Problem;
import team.beatcode.qbank.repository.ProblemRepository;
import team.beatcode.qbank.utils.Macros;

@Repository
public class ProblemDaoImp implements team.beatcode.qbank.dao.ProblemDao {
    ProblemRepository problemRepository;
    @Autowired
    MongoTemplate mongoTemplate;

    public ProblemDaoImp(ProblemRepository problemRepository) {
        this.problemRepository = problemRepository;
    }


    /**
     * 高级查找
     * @param title 标题包含此内容，可以填空串
     * @param difficulty 难度，为空时返回全部
     * @param page 页码，从0开始
     * @param perPage 每页大小
     * @return List
     */
    @Override
    public Page<Problem> findByAll(String title, String difficulty, Integer page, Integer perPage) {
        Pageable pageable = PageRequest.of(page, perPage);
        return problemRepository
                .findProblemByTitleNameContainingAndDifficultyContainingOrderByTitleIdAsc(
                        title, difficulty, pageable
                );
    }

    @Override
    public Problem findProblemById(Integer id) {
        return problemRepository.findProblemByTitleId(id);
    }

    @Override
    public String generateConfig(Problem problem) {
        if (problem == null)
            return null;
        Integer tLInMilli = problem.getConfig().getTLimit();
        return switch (problem.getConfig().getType()) {
            case Macros.ProblemType.CONVENTIONAL ->
                    String.format("%s %s\n%s %s\n%s %d\n%s %d.%d\n%s %d",
                    /* 测评设置 */
                    "use_builtin_judger", problem.getConfig().getBuiltinJ(),
                    "use_builtin_checker", problem.getConfig().getBuiltinC(),
                    /* 测试设置 */
                    "n_tests", problem.getConfig().getTests(),
                    /* 限制设置 */
                    "time_limit", tLInMilli / 1000, tLInMilli % 1000,
                    "memory_limit", problem.getConfig().getMLimit()
            );
            default -> null;
        };
    }
}
