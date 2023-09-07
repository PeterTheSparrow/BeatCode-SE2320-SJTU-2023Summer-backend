package team.beatcode.user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import team.beatcode.user.entity.UserCondition;

import java.util.Map;

public interface ConditionService {
    UserCondition GetUserCondition(String user_id);
    Page<UserCondition> getPaginatedRanking(Map<String,String> SearchMaps);
    UserCondition SaveUserCondition(UserCondition userCondition);
}
