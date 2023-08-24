package team.beatcode.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "user_condition")
public class UserCondition {

    @Id
    @Field("_id")
    private ObjectId id;
    @Field("problem_condition")
    private String problemCondition;
    @Field("user_id")
    private String userId;
    @Field("ACount")
    private int ACount;
}
