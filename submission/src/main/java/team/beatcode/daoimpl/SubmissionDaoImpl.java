package team.beatcode.daoimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import team.beatcode.dao.SubmissionDao;
import team.beatcode.entity.Submission;
import team.beatcode.repository.SubmissionRepository;

import java.util.List;
import java.util.Map;

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
    @Override
    public Page<Submission> findByUname(String uname, Pageable pageable)
    {return submissionRepository.findAllByUserName(uname,pageable);}
    @Override
    public Page<Submission> findByPid(String pid, Pageable pageable)
    {return submissionRepository.findAllByProblemId(pid,pageable);}
    @Override
    public Page<Submission> findByPname(String pname, Pageable pageable)
    {return submissionRepository.findAllByProblemNameContaining(pname,pageable);}

    @Override
    public Page<Submission> findByUnameAndPid(String uname,String pid, Pageable pageable)
    {return submissionRepository.findAllByUserNameAndProblemId(uname,pid,pageable);}
    @Override
    public Page<Submission> findByUnameAndPname(String uname,String pname, Pageable pageable)
    {return submissionRepository.findAllByUserNameAndProblemNameContaining(uname,pname,pageable);}
    @Override
    public Page<Submission> findByPidAndPname(String pid,String pname, Pageable pageable)
    {return submissionRepository.findAllByProblemIdAndProblemNameContaining(pid,pname,pageable);}

    @Override
    public Page<Submission> findByUnameAndPidAndPname(String uname,String pid,String pname, Pageable pageable)
    {return submissionRepository.findAllByUserNameAndProblemIdAndProblemNameContaining(uname,pid,pname,pageable);}
    @Override
    public Page<Submission> findAll(Pageable pageable)
    {return submissionRepository.findAll(pageable);}
    @Override
    public Page<Submission> findByStateAndProblemId(String state,String problem_id,Pageable pageable)
    {
        return submissionRepository.findAllByStateAndProblemId(state,problem_id,pageable);
    }
}
