package team.beatcode.qbank.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import team.beatcode.qbank.entity.Problem;
import java.util.List;

public interface ProblemRepository extends MongoRepository<Problem, String> {
    // 根据title模糊搜索适配的题目
    List<Problem> findProblemsByTitleContaining(String title);

}
