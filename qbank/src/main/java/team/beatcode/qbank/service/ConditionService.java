package team.beatcode.qbank.service;

import team.beatcode.qbank.entity.UserCondition;
import team.beatcode.qbank.utils.msg.MessageException;

public interface ConditionService {
    UserCondition GetUserCondition(String user_id);
}
