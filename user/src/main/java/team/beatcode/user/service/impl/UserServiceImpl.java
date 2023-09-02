package team.beatcode.user.service.impl;

import team.beatcode.user.dao.UserDao;
import team.beatcode.user.entity.Person_info;
import team.beatcode.user.entity.User;
import team.beatcode.user.entity.User_info;
import team.beatcode.user.entity.User_record;
import team.beatcode.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserDao userDao;

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
    public Person_info getUserInfo(Integer userId) {
        return userDao.getUserInfo(userId);
    }

    @Override
    public void updateUserName(Integer userId, String userName) {
        userDao.updateUserName(userId, userName);
    }

    @Override
    public void updatePassword(Integer userId, String password) {
        userDao.updatePassword(userId, password);
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

}
