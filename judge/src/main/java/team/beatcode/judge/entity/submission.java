package team.beatcode.judge.entity;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

import java.util.List;

@Document(collection = "submission_mongo")
public class submission {
    @Id
    private String _id;
    private int submission_id;
    private String submission_code;

    private int problem_id;
    private int result_n;
    private List<result>submission_result;

    private String conf;

}
