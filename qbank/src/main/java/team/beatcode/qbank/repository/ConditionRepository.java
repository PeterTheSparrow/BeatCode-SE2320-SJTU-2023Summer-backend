package team.beatcode.qbank.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import team.beatcode.qbank.entity.UserCondition;

public interface ConditionRepository extends MongoRepository<UserCondition, String> {
    UserCondition findByUserId(String userId);
}
