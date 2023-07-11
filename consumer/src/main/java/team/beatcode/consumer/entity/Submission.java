package team.beatcode.consumer.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;


@Data
public class Submission {
    private ObjectId _id;
    private String string_id;

    private String submission_code;
    private String submission_language;
    private String submission_time;

    private String userId;
    private String userName;
    private String problemId;
    private String problemName;

    private String conf;
    private int case_n;
    private List<Result> details;

    private String result_score;
    private String result_time;
    private String result_memory;
    private String full_result;
}