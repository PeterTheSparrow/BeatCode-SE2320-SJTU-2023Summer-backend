package team.beatcode.user.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.LinkedHashMap;

@Data
@Document(collection = "user_condition")
public class UserCondition {

    @Id
    @Field("_id")
    private String _id;
    @Field("user_id")
    private String userId;
    @Field("user_name")
    private String userName;
    @Field("ACount")
    private int ACount;
    /**
     * Key: 题号
     * Value: 分数
     */
    @Field("problem_condition")
    private LinkedHashMap<String, Integer> problemCondition;
    /**
     * Key: 日期
     * Value: 提交数
     */
    @Field("User_activity")
    private LinkedHashMap<String, Integer> UserActivity;
}
