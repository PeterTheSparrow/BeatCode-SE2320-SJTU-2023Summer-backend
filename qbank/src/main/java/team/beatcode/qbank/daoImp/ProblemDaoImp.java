package team.beatcode.qbank.daoImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import team.beatcode.qbank.entity.Problem;
import team.beatcode.qbank.repository.ProblemRepository;

import java.util.List;

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
    public List<Problem> findByAll(String title, String difficulty, Integer page, Integer perPage) {
        Pageable pageable = PageRequest.of(page, perPage);
        Page<Problem> problems = problemRepository
                .findProblemByTitleNameContainingAndDifficultyContaining(
                        title, difficulty, pageable
                );
        if (problems == null) {
            return null;
        }
        else
            return problems.stream().toList();
    }

    @Override
    public Problem findProblemById(Integer id) {
        return problemRepository.findProblemByTitleId(id);
    }
}
