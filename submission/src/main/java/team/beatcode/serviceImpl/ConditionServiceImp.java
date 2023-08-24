package team.beatcode.serviceImpl;

import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import team.beatcode.dao.ConditionDao;
import team.beatcode.entity.UserCondition;
import team.beatcode.service.ConditionService;

@Service
public class ConditionServiceImp implements ConditionService {
    ConditionDao conditionDao;

    public ConditionServiceImp(ConditionDao conditionDao) {
        this.conditionDao = conditionDao;
    }

    @Override
    public UserCondition GetUserCondition(String user_id)
    {
        return conditionDao.GetUserCondition(user_id);
    }
    @Override
    public UserCondition SaveUserCondition(UserCondition userCondition){return conditionDao.SaveUserCondition(userCondition);}
}
