package team.beatcode.user.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import team.beatcode.user.dao.UserDao;
import team.beatcode.user.entity.User_info;
import team.beatcode.user.entity.User_admin;
import team.beatcode.user.repository.User_infoRepository;
import team.beatcode.user.repository.User_adminRepository;

import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    User_infoRepository user_infoRepository;
    User_adminRepository user_adminRepository;

    @Override
    public User_info getUser_info(Integer id){
        Optional<User_info> user_info = user_infoRepository.findUser_infoByuserId(id);
        return user_info.orElse(null);
    }

    @Override
    public User_admin getUser_admin(Integer id){
        Optional<User_admin> user_admin = user_adminRepository.findUser_adminByuserId(id);
        return user_admin.orElse(null);
    }

}
