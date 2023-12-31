package team.beatcode.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.beatcode.dao.ProblemDao;
import team.beatcode.entity.Problem;
import team.beatcode.service.ProblemService;

@Service
public class ProblemServiceImpl implements ProblemService {
    @Autowired
    private ProblemDao problemDao;
    public Problem getProblem(int pid)
    {
        return problemDao.findByPid(pid);
    }
}
