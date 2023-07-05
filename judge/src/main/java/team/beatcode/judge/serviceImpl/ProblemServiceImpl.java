package team.beatcode.judge.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.beatcode.judge.dao.ProblemDao;
import team.beatcode.judge.entity.Problem;
import team.beatcode.judge.service.ProblemService;
@Service
public class ProblemServiceImpl implements ProblemService {
    @Autowired
    private ProblemDao problemDao;
    public Problem getProblem(int pid)
    {
        return problemDao.findByPid(pid);
    }
}
