package team.beatcode.judge.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "submission_mongo")
@Data
public class Submission {
    @Id
    private String _id;
    private String string_id;

    private String submission_code;
    private String submission_language;
    private String submission_time;

    @Field("user_id")
    private String userId;
    @Field("user_name")
    private String userName;
    @Field("problem_id")
    private String problemId;
    @Field("problem_name")
    private String problemName;

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
