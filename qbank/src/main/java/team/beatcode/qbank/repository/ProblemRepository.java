package team.beatcode.qbank.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import team.beatcode.qbank.entity.Problem;

public interface ProblemRepository extends MongoRepository<Problem, String> {
}
