package team.beatcode.user.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import team.beatcode.user.dao.ConditionDao;
import team.beatcode.user.entity.UserCondition;
import team.beatcode.user.repository.ConditionRepository;

@Repository
public class ConditionDaoImp implements ConditionDao {
    @Autowired
    ConditionRepository conditionRepository;
    @Override
    public UserCondition GetUserCondition(String user_id)
    {
        return conditionRepository.findByUserId(user_id);
    }
    @Override
    public Page<UserCondition> findAll(Pageable pageable){return conditionRepository.findAll(pageable);}
    @Override
    public UserCondition SaveUserCondition(UserCondition userCondition){return conditionRepository.save(userCondition);}
}
