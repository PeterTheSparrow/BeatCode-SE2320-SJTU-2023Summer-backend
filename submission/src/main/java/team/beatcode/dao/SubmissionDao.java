package team.beatcode.dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import team.beatcode.entity.Submission;

import java.util.List;

public interface SubmissionDao {
    Submission findBySid(String sid);
    void SaveResult(Submission res);
    Page<Submission> findByUname(String uname, Pageable pageable);
    Page<Submission> findByPid(String pid, Pageable pageable);
    Page<Submission> findByPname(String pname, Pageable pageable);

    Page<Submission> findByUnameAndPid(String uname,String pid, Pageable pageable);
    Page<Submission> findByUnameAndPname(String uname,String pname, Pageable pageable);
    Page<Submission> findByPidAndPname(String pid,String pname, Pageable pageable);

    Page<Submission> findByUnameAndPidAndPname(String uname,String pid,String pname, Pageable pageable);
    Page<Submission> findAll(Pageable pageable);

}
