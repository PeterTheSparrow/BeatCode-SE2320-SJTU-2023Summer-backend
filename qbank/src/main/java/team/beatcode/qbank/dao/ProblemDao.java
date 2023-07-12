package team.beatcode.qbank.dao;

import team.beatcode.qbank.entity.Problem;

import java.util.List;

public interface ProblemDao {

    List<Problem> findByAll
            (String title, String difficulty, Integer page, Integer perPage);
    Problem findProblemById(Integer id);
}
