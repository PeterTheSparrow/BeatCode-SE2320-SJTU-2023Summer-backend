package team.beatcode.judge.daoimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import team.beatcode.judge.dao.SubmissionDao;
import team.beatcode.judge.entity.Submission;
import team.beatcode.judge.repository.SubmissionRepository;

@Repository
public class SubmissionDaoImpl implements SubmissionDao {
    @Autowired
    private SubmissionRepository submissionRepository;
    @Override
    public Submission findBySid(int sid)
    {
        return submissionRepository.findBySubmission_id(sid);
    }
    @Override
    public void SaveResult(Submission res)
    {
        submissionRepository.save(res);
    }
}
