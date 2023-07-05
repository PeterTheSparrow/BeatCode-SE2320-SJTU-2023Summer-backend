package team.beatcode.judge.daoimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import team.beatcode.judge.dao.ProblemDao;
import team.beatcode.judge.entity.Problem;
import team.beatcode.judge.repository.ProblemRepository;

@Repository
public class ProblemDaoImpl implements ProblemDao {
    @Autowired
    private ProblemRepository problemRepository;
    @Override
    public Problem findByPid(int pid)
    {
        return problemRepository.findProblemByProblem_id(pid);
    }
}
