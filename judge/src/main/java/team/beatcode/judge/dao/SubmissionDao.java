package team.beatcode.judge.dao;

import team.beatcode.judge.entity.Submission;

public interface SubmissionDao {
    public Submission findBySid(String sid);
    public void SaveResult(Submission res);
}
