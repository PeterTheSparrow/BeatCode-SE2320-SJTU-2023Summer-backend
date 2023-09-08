package team.beatcode.service;

import org.springframework.data.domain.Page;
import team.beatcode.entity.Submission;

import java.util.Map;

public interface SubmissionService {
    public Submission getSubmission(String sid);
    public void saveSubmission(Submission sub);
    Page<Submission> getPaginatedSubmissions(Map<String,String> SearchMaps);
    Page<Submission> getPaginatedProblemSubmissions(Map<String,String> SearchMaps);
}
