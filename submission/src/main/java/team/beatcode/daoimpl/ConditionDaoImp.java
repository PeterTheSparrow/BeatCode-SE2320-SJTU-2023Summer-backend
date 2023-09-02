package team.beatcode.daoimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import team.beatcode.entity.UserCondition;
import team.beatcode.repository.ConditionRepository;

@Repository
public class ConditionDaoImp implements team.beatcode.dao.ConditionDao {
    ConditionRepository conditionRepository;
    @Autowired
    MongoTemplate mongoTemplate;

    public ConditionDaoImp(ConditionRepository conditionRepository) {
        this.conditionRepository = conditionRepository;
    }

    @Override
    public UserCondition GetUserCondition(String user_id)
    {
        return conditionRepository.findByUserId(user_id);
    }
    @Override
    public UserCondition SaveUserCondition(UserCondition userCondition){return conditionRepository.save(userCondition);}
}
