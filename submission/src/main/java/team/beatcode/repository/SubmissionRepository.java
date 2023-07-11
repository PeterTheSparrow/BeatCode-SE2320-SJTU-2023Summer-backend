package team.beatcode.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import team.beatcode.entity.Submission;

public interface SubmissionRepository extends MongoRepository<Submission,String> {
    Submission findBy_id(String sid);
    Page<Submission> findAllByUserName(String user_name, Pageable pageable);
    Page<Submission> findAllByProblemId(String problem_id, Pageable pageable);
    Page<Submission> findAllByProblemName(String problem_name,Pageable pageable);

    Page<Submission> findAllByUserNameAndProblemId(String user_name,String problem_id,Pageable pageable);
    Page<Submission> findAllByUserNameAndProblemName(String user_name,String problem_name,Pageable pageable);
    Page<Submission> findAllByProblemIdAndProblemName(String problem_id,String problem_name,Pageable pageable);

    Page<Submission> findAllByUserNameAndProblemIdAndProblemName(String username,String problem_id,String problem_name,Pageable pageable);
}
