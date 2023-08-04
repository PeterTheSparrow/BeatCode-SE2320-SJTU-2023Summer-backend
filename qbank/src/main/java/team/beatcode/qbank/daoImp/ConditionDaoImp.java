package team.beatcode.qbank.daoImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import team.beatcode.qbank.entity.UserCondition;
import team.beatcode.qbank.repository.ConditionRepository;
import team.beatcode.qbank.utils.Macros;

@Repository
public class ConditionDaoImp implements team.beatcode.qbank.dao.ConditionDao {
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
}
