package team.beatcode.qbank.dao;

import org.springframework.data.domain.Page;
import team.beatcode.qbank.entity.Problem;
import team.beatcode.qbank.entity.UserCondition;

public interface ConditionDao {
    UserCondition GetUserCondition(String user_id);
}
