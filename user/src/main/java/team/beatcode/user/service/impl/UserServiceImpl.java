package team.beatcode.user.service.impl;

import team.beatcode.user.dao.UserDao;
import team.beatcode.user.entity.User;
import team.beatcode.user.entity.User_auth;
import team.beatcode.user.entity.User_info;
import team.beatcode.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserDao userDao;

    @Override
    public User getUser(Integer id){
        User_info userInfo = userDao.getUser_info(id);
        User_auth userAdmin = userDao.getUser_auth(id);

        User user = new User();
        user.setUser_name(userAdmin.getUser_name());
        user.setPassword(userAdmin.getPassword());
        user.setEmail(userInfo.getEmail());
        user.setPhone(userInfo.getPhone());

        return user;
    }

}
