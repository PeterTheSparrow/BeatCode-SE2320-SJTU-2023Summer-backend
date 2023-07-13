package team.beatcode.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import team.beatcode.entity.Problem;

public interface ProblemRepository extends MongoRepository<Problem,String> {
    Problem findProblemByTitleId(int pid);
}
