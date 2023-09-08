package team.beatcode.submission.service;

import org.springframework.data.domain.Page;
import team.beatcode.submission.entity.Submission;

import java.util.Map;

public interface SubmissionService {
    public Submission getSubmission(String sid);
    public String saveSubmission(Submission sub);
    Page<Submission> getPaginatedSubmissions(Map<String,String> SearchMaps);
    Page<Submission> getPaginatedProblemSubmissions(Map<String,String> SearchMaps);
}
