package team.beatcode.judge.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import team.beatcode.judge.entity.Problem;

public interface ProblemRepository extends MongoRepository<Problem,String> {
    @Query("{ 'problem_id' : ?0 }")
    Problem findProblemByProblem_id(int pid);
}
