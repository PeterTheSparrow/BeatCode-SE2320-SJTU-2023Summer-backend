package team.beatcode.judge.service;

import team.beatcode.judge.entity.Submission;

public interface SubmissionService {
    public Submission getSubmission(String sid);
    public void saveSubmission(Submission sub);
}
