package team.beatcode.judge.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class Submission {
    @Id
    private ObjectId _id;
    private String string_id;

    private String submission_code;
    private String submission_language;
    private String submission_time;

    private String problem_id;
    private String problem_name;
    private String user_name;
    private String user_id;

    private String conf;
    private int case_n;

    private List<Result>details;
    private String result_score;
    private String result_time;
    private String result_memory;
    private String full_result;

    private String state;
    private String error;
}
