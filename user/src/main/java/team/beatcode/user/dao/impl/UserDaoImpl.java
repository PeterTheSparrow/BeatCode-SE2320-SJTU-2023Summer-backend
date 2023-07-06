package team.beatcode.user.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import team.beatcode.user.dao.UserDao;
import team.beatcode.user.entity.User_info;
import team.beatcode.user.entity.User_auth;
import team.beatcode.user.entity.User_record;
import team.beatcode.user.repository.User_infoRepository;
import team.beatcode.user.repository.User_recordRepository;

import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    User_infoRepository user_infoRepository;

    @Autowired
    User_recordRepository user_recordRepository;

    @Override
    public User_info getUser_info(Integer userId){
        Optional<User_info> user_info = user_infoRepository.findUser_infoByUserId(userId);
        return user_info.orElse(null);
    }

//    @Override
//    public User_auth getUser_auth(Integer userId){
//        Optional<User_auth> user_auth = user_authRepository.findUser_authByUserId(userId);
//        return user_auth.orElse(null);
//    }

    @Override
    public User_record getUser_record(Integer userId){
        Optional<User_record> user_record = user_recordRepository.findUser_recordByUserId(userId);
        return user_record.orElse(null);
    }

}
