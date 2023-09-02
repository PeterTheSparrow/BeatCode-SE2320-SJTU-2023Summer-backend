package team.beatcode.user.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import team.beatcode.user.entity.UserCondition;

public interface ConditionRepository extends MongoRepository<UserCondition, String> {
    UserCondition findByUserId(String userId);
}
