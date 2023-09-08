package team.beatcode.submission.dao;

import team.beatcode.submission.entity.Problem;

public interface ProblemDao {
    Problem findByPid(int pid);
}
