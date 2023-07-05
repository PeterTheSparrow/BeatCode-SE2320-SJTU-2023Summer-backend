package team.beatcode.judge.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import team.beatcode.judge.entity.Submission;

public interface SubmissionRepository extends MongoRepository<Submission,String> {
    @Query("{ 'submission_id' : ?0 }")
    Submission findBySubmission_id(int sid);
}
