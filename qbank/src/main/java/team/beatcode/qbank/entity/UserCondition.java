package team.beatcode.qbank.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "user_condition")
public class UserCondition {

    @Id
    @Field("_id")
    private String id;
    @Field("problem_condition")
    private String problemCondition;
    @Field("user_id")
    private String userId;
}
