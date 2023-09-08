package team.beatcode.submission.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import team.beatcode.submission.entity.Problem;

public interface ProblemRepository extends MongoRepository<Problem,String> {
    Problem findProblemByTitleId(int pid);
}
