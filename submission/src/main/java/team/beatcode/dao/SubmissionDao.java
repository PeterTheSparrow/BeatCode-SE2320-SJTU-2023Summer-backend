package team.beatcode.dao;


import team.beatcode.entity.Submission;

public interface SubmissionDao {
    public Submission findBySid(String sid);
    public void SaveResult(Submission res);
}
