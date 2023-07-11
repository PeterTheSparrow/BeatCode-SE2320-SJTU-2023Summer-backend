package team.beatcode.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import team.beatcode.dao.SubmissionDao;
import team.beatcode.entity.Submission;
import team.beatcode.service.SubmissionService;

import java.util.Map;

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
        sub.getStringId();
        submissionDao.SaveResult(sub);
    }
    @Override
    public Page<Submission> getPaginatedSubmissions(Map<String,String> SearchMaps){
        String sortDirection=SearchMaps.get("sortDirection");//asc/desc
        if(sortDirection==null)sortDirection="desc";

        String sortBy=SearchMaps.get("sortBy");
        if(sortBy==null)sortBy="submission_time";

        String user_name=SearchMaps.get("user_name");
        String problem_id=SearchMaps.get("problem_id");
        String problem_name=SearchMaps.get("problem_name");

        int page=Integer.parseInt(SearchMaps.get("page"));
        int pageSize=Integer.parseInt(SearchMaps.get("pageSize"));

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page - 1, pageSize, sort);

        if(user_name!=null && problem_id!=null && problem_name!=null)
        {
            return submissionDao.findByUnameAndPidAndPname(user_name,problem_id,problem_name,pageable);
        }
        else if(user_name!=null && problem_id!=null)
        {
            return submissionDao.findByUnameAndPid(user_name,problem_id,pageable);
        }
        else if(user_name!=null && problem_name!=null)
        {
            return submissionDao.findByUnameAndPname(user_name,problem_name,pageable);
        }
        else if(problem_id!=null && problem_name!=null)
        {
            return submissionDao.findByPidAndPname(problem_id,problem_name,pageable);
        }
        else if(user_name!=null)
        {
            return submissionDao.findByUname(user_name,pageable);
        }
        else if(problem_id!=null)
        {
            return submissionDao.findByPid(problem_id,pageable);
        }
        else if(problem_name!=null)
        {
            return submissionDao.findByPname(problem_name,pageable);
        }
        else
        {
            return submissionDao.findAll(pageable);
        }
    }
}
