package team.beatcode.judge.service;

import team.beatcode.judge.entity.Submission;

public interface SubmissionService {
    public Submission getSubmission(int sid);
    public void saveSubmission(Submission sub);
}
