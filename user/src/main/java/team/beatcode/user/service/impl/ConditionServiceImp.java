package team.beatcode.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import team.beatcode.user.dao.ConditionDao;
import team.beatcode.user.entity.UserCondition;
import team.beatcode.user.service.ConditionService;

import java.util.Map;

@Service
public class ConditionServiceImp implements ConditionService {
    @Autowired
    ConditionDao conditionDao;

    @Override
    public UserCondition GetUserCondition(String user_id)
    {
        return conditionDao.GetUserCondition(user_id);
    }
    @Override
    public Page<UserCondition> getPaginatedRanking(Map<String,String> SearchMaps){
        String sortDirection="desc";
        String sortBy="ACount";
        int page=Integer.parseInt(SearchMaps.get("page"));
        int pageSize=Integer.parseInt(SearchMaps.get("pageSize"));

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page - 1, pageSize, sort);
        return conditionDao.findAll(pageable);
    }

}
