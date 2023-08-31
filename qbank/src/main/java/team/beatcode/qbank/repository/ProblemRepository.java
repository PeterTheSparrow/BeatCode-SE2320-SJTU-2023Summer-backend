package team.beatcode.qbank.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import team.beatcode.qbank.entity.Problem;

public interface ProblemRepository extends MongoRepository<Problem, String> {

    Page<Problem> findProblemByTitleNameContainingAndDifficultyContainingOrderByTitleIdAsc(String title_name, String difficulty, Pageable pageable);

    Problem findProblemByTitleId(Integer id);

    VersionProjection findVersionProjectionByTitleId(Integer id);
}
