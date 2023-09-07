package team.beatcode.entity;

import lombok.Data;

import java.util.LinkedHashMap;

@Data
public class UserCondition {

    private String _id;
    private String userId;
    private String userName;
    private int ACount;
    /**
     * Key: 题号
     * Value: 分数
     */
    private LinkedHashMap<String, Integer> problemCondition;
    /**
     * Key: 日期
     * Value: 提交数
     */
    private LinkedHashMap<String, Integer> UserActivity;
}
