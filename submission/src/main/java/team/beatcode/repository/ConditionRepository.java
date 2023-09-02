package team.beatcode.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import team.beatcode.entity.UserCondition;

public interface ConditionRepository extends MongoRepository<UserCondition, String> {
    UserCondition findByUserId(String userId);
}
