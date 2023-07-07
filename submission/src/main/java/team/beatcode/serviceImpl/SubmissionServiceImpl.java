package team.beatcode.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.beatcode.dao.SubmissionDao;
import team.beatcode.entity.Submission;
import team.beatcode.service.SubmissionService;
@Service
public class SubmissionServiceImpl implements SubmissionService {
    @Autowired
    private SubmissionDao submissionDao;
    @Override
    public Submission getSubmission(String sid)
    {
        return submissionDao.findBySid(sid);
    }
    @Override
    public void saveSubmission(Submission sub)
    {
        submissionDao.SaveResult(sub);
    }
}
