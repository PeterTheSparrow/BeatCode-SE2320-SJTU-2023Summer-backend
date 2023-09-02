package team.beatcode.qbank.serviceImp;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import team.beatcode.qbank.dao.ConditionDao;
import team.beatcode.qbank.entity.UserCondition;
import team.beatcode.qbank.service.ConditionService;
import team.beatcode.qbank.utils.Macros;
import team.beatcode.qbank.utils.msg.MessageEnum;
import team.beatcode.qbank.utils.msg.MessageException;

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
}
