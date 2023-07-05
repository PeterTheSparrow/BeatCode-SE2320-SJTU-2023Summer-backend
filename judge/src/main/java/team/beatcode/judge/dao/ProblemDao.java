package team.beatcode.judge.dao;

import team.beatcode.judge.entity.Problem;

public interface ProblemDao {
    Problem findByPid(int pid);
}
