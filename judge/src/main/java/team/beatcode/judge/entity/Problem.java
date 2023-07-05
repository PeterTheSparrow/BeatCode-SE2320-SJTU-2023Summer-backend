package team.beatcode.judge.entity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

import java.util.List;

@Document(collection = "problem_mongo")
@Data
public class Problem {
    @Id
    private String _id;

    private int problem_id;
    private int case_n;
    private List<Testcase> test_cases;

    private String conf;

}
