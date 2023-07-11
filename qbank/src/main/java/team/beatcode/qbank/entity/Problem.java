package team.beatcode.qbank.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Setter
@Getter
@Data
@Document(collection = "problem_mongo")
public class Problem {
    @Id
    private String id;

    private Integer case_n;
    private String conf;
    private String memory_limit;
    private Integer problem_id;

    private List<Tag> tags;
    private String output_limit;
    private String checker;
    private String description;
    private String time_limit;
    private String title;
    private String difficulty;
}
