package team.beatcode.daoimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import team.beatcode.dao.ProblemDao;
import team.beatcode.repository.ProblemRepository;
import team.beatcode.entity.Problem;

@Repository
public class ProblemDaoImpl implements ProblemDao {
    @Autowired
    private ProblemRepository problemRepository;
    @Override
    public Problem findByPid(int pid)
    {
        return problemRepository.findProblemByTitleId(pid);
    }
}
