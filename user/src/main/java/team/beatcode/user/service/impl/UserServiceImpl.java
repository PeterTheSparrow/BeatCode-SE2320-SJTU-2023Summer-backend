package team.beatcode.user.service.impl;

import org.springframework.data.domain.Page;
import team.beatcode.qbank.entity.Problem;
import team.beatcode.qbank.utils.msg.MessageEnum;
import team.beatcode.qbank.utils.msg.MessageException;
import team.beatcode.user.dao.UserDao;
import team.beatcode.user.entity.*;
import team.beatcode.user.feign.QbankFeign;
import team.beatcode.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserDao userDao;

    @Autowired
    QbankFeign qbankFeign;

    @Override
    public User getUser(Integer userId){
        User_info userInfo = userDao.getUser_info(userId);
        User_record userRecord = userDao.getUser_record(userId);

        User user = new User();
        user.setUserId(userInfo.getUserId());
        user.setUserName(userInfo.getUserName());
        user.setEmail(userInfo.getEmail());
        user.setPhone(userInfo.getPhone());
        user.setAccept_num(userRecord.getAcceptNum());
        user.setAccept_submit(userRecord.getAcceptSubmit());
        user.setSubmit_num(userRecord.getSubmitNum());

        return user;
    }

    @Override
    public void register(Integer userId, String userName, String email, String phone) {
        userDao.register(userId, userName, email, phone);
    }

    @Override
    public List<User_record> getRanks(){
        return userDao.getRecords();
    }

    @Override
    public Boolean checkUserExist(Integer userId) {return userDao.checkUserExist(userId); }

    @Override
    public Person_info getUserInfo(Integer userId) {
        // 这里直接查表就可以了
        return userDao.getUserInfo(userId);
    }

    @Override
    public Boolean updateUserName(Integer userId, String userName) {
        // 检查用户名是否重复
        if (userDao.checkUserNameExist(userName)) {
            return false;
        }
        userDao.updateUserName(userId, userName);
        return true;
    }


    @Override
    public void updatePhone(Integer userId, String phone) {
        userDao.updatePhone(userId, phone);
    }

    @Override
    public Boolean checkEmailExist(String email) {
        return userDao.checkEmailExist(email);
    }

    @Override
    public void updateEmail(Integer userId, String email) {
        userDao.updateEmail(userId, email);
    }

    @Override
    public Boolean checkUserNameExist(String userName) {
        return userDao.checkUserNameExist(userName);
    }

    @Override
    public UserCondition getUserCondition(String userId) {return userDao.getUserCondition(userId);}

    @Override
    public User_problem.Paged getProblemList(Integer pageIndex, Integer pageSize, String problemCondition) {
        Map<String, Object> map = new HashMap<>();
        map.put("pageIndex", pageIndex);
        map.put("pageSize", pageSize);
        map.put("problemCondition", problemCondition);
        Page<Problem> problems = qbankFeign.getUserProblem(map);

        if(problems==null){
            throw new MessageException(MessageEnum.SEARCH_PAGE_FAULT);
        }

        return new User_problem.Paged(
                problems.stream().map(User_problem::new).toList(),
                problems.getTotalPages());
    }

    @Override
    public List<User_activity> getUserActivity(String userActivity, String year) {
        List<User_activity> userActivities = new ArrayList<>();
        String regex = "<(" + year + "/[0-9]{2}/[0-9]{2})>([0-9]+)</\\1>";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(userActivity);

        while (matcher.find()) {
            User_activity userActivity1 = new User_activity();
            userActivity1.setDate(matcher.group(1));
            userActivity1.setCount(Integer.parseInt(matcher.group(2)));
            userActivities.add(userActivity1);
        }

        return userActivities;
    }

}
