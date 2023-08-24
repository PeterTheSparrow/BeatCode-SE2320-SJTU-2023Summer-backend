package team.beatcode.dao;

import team.beatcode.entity.UserCondition;

public interface ConditionDao {
    UserCondition GetUserCondition(String user_id);
    UserCondition SaveUserCondition(UserCondition userCondition);
}
