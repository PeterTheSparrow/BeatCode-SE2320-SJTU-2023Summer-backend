package team.beatcode.submission.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.beatcode.submission.dao.ProblemDao;
import team.beatcode.submission.entity.Problem;
import team.beatcode.submission.service.ProblemService;

@Service
public class ProblemServiceImpl implements ProblemService {
    @Autowired
    private ProblemDao problemDao;
    public Problem getProblem(int pid)
    {
        return problemDao.findByPid(pid);
    }
}
