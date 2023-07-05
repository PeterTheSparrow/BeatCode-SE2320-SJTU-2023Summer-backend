package team.beatcode.judge.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.beatcode.judge.dao.SubmissionDao;
import team.beatcode.judge.entity.Submission;
import team.beatcode.judge.service.SubmissionService;
@Service
public class SubmissionServiceImpl implements SubmissionService {
    @Autowired
    private SubmissionDao submissionDao;
    @Override
    public Submission getSubmission(int sid)
    {
        return submissionDao.findBySid(sid);
    }
    @Override
    public void saveSubmission(Submission sub)
    {
        submissionDao.SaveResult(sub);
    }
}
