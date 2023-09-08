package team.beatcode.submission.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import team.beatcode.submission.entity.Submission;

public interface SubmissionRepository extends MongoRepository<Submission,String> {
    Submission findBy_id(String sid);
    Page<Submission> findAllByUserName(String user_name, Pageable pageable);
    Page<Submission> findAllByProblemId(String problem_id, Pageable pageable);
    Page<Submission> findAllByProblemNameContaining(String problem_name,Pageable pageable);

    Page<Submission> findAllByUserNameAndProblemId(String user_name,String problem_id,Pageable pageable);
    Page<Submission> findAllByUserNameAndProblemNameContaining(String user_name,String problem_name,Pageable pageable);
    Page<Submission> findAllByProblemIdAndProblemNameContaining(String problem_id,String problem_name,Pageable pageable);

    Page<Submission> findAllByUserNameAndProblemIdAndProblemNameContaining(String username,String problem_id,String problem_name,Pageable pageable);
    Page<Submission> findAllByStateAndProblemId(String state,String problem_id,Pageable pageable);
}
