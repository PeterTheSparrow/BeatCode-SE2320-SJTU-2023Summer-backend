package team.beatcode.qbank.dao;

import org.springframework.data.domain.Page;
import team.beatcode.qbank.entity.Problem;

import java.util.List;

public interface ProblemDao {

    Page<Problem> findByAll
            (String title, String difficulty, Integer page, Integer perPage);
    Problem findProblemById(Integer id);
    Integer findVersionById(Integer id);

    Page<Problem> findByIds(List<Integer> ProblemIds, Integer pageIndex, Integer pageSize);
}
