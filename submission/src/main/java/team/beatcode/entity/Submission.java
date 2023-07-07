package team.beatcode.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "submission_mongo")
@Data
public class Submission {
    @Id
    private String _id;
    private String submission_code;
    private String submission_language;

    private int user_id;
    private String user_name;
    private int problem_id;
    private String problem_name;

    private String conf;
    public void build_conf()
    {
        conf="problem_id "+ problem_id+"\n"+"language "+submission_language;
    }
}
