package team.beatcode.judge.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import team.beatcode.judge.entity.Submission;

public interface SubmissionRepository extends MongoRepository<Submission,String> {
    @Query("{ '_id' : ?0 }")
    Submission findBy_id(String sid);
}
