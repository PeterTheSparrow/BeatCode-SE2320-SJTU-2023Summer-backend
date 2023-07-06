package team.beatcode.user.service.impl;

import team.beatcode.user.dao.UserDao;
import team.beatcode.user.entity.User;
import team.beatcode.user.entity.User_auth;
import team.beatcode.user.entity.User_info;
import team.beatcode.user.entity.User_record;
import team.beatcode.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserDao userDao;

    @Override
    public User getUser(Integer userId){
        User_info userInfo = userDao.getUser_info(userId);
//        User_auth userAuth = userDao.getUser_auth(userId);
        User_record userRecord = userDao.getUser_record(userId);

        User user = new User();
//        user.setUser_name(userAuth.getUser_name());
//        user.setPassword(userAuth.getPassword());
        user.setUserName(userInfo.getUserName());
        user.setEmail(userInfo.getEmail());
        user.setPhone(userInfo.getPhone());
        user.setAccept_num(userRecord.getAccept_num());
        user.setAccept_submit(userRecord.getAccept_submit());
        user.setSubmit_num(userRecord.getSubmit_num());

        return user;
    }

    @Override
    public void register(String userName, String email, String phone) {
        User user = new User();
        user.setUserName(userName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setAccept_num(0);
        user.setAccept_submit(0);
        user.setSubmit_num(0);
    }

//    @Override
//    List<User_record> getRanks(){
//
//
//    }

}
