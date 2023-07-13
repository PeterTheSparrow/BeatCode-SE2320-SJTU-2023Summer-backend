package team.beatcode.dao;

import team.beatcode.entity.Problem;

public interface ProblemDao {
    Problem findByPid(int pid);
}
