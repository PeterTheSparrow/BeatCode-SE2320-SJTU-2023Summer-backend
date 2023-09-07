package team.beatcode.user.service;

import org.springframework.data.domain.Page;
import team.beatcode.user.entity.UserCondition;

import java.util.Map;

public interface ConditionService {
    UserCondition GetUserCondition(String user_id);
    Page<UserCondition> getPaginatedRanking(Map<String,String> SearchMaps);
    void saveUserCondition(UserCondition userCondition);
}
