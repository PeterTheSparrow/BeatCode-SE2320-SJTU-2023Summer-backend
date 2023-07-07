package team.beatcode.service;

import team.beatcode.entity.Submission;

public interface SubmissionService {
    public Submission getSubmission(String sid);
    public void saveSubmission(Submission sub);
}
