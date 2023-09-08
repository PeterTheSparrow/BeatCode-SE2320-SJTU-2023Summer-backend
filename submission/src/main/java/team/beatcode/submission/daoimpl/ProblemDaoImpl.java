package team.beatcode.submission.daoimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import team.beatcode.submission.dao.ProblemDao;
import team.beatcode.submission.repository.ProblemRepository;
import team.beatcode.submission.entity.Problem;

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
