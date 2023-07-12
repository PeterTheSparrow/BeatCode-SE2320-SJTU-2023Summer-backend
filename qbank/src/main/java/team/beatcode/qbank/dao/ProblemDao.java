package team.beatcode.qbank.dao;

import org.springframework.data.domain.Page;
import team.beatcode.qbank.entity.Problem;

public interface ProblemDao {

    Page<Problem> findByAll
            (String title, String difficulty, Integer page, Integer perPage);
    Problem findProblemById(Integer id);
}
