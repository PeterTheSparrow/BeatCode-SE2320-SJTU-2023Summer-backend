package team.beatcode.service;

import team.beatcode.entity.UserCondition;

public interface ConditionService {
    UserCondition GetUserCondition(String user_id);
    UserCondition SaveUserCondition(UserCondition userCondition);
}
