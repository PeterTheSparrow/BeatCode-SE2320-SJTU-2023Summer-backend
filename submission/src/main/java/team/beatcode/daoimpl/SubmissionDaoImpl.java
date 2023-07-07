package team.beatcode.daoimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import team.beatcode.dao.SubmissionDao;
import team.beatcode.entity.Submission;
import team.beatcode.repository.SubmissionRepository;

@Repository
public class SubmissionDaoImpl implements SubmissionDao {
    @Autowired
    private SubmissionRepository submissionRepository;
    @Override
    public Submission findBySid(String sid)
    {
        return submissionRepository.findBy_id(sid);
    }
    @Override
    public void SaveResult(Submission res)
    {
        submissionRepository.save(res);
    }
}
