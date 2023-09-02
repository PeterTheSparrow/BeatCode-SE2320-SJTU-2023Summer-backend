package team.beatcode.user.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import team.beatcode.user.entity.UserCondition;

public interface ConditionDao {
    UserCondition GetUserCondition(String user_id);
    Page<UserCondition> findAll(Pageable pageable);
}
