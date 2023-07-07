package team.beatcode.judge.entity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

import java.util.List;

@Document(collection = "submission_mongo")
@Data
public class Submission {
    @Id
    private String _id;
    private String submission_code;

    private int problem_id;
    private int case_n;
    private List<Result>details;
    private String result_score;
    private String result_time;
    private String result_memory;
    private String conf;
    private String full_result;
}
