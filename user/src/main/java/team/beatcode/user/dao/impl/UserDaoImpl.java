package team.beatcode.user.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import team.beatcode.user.dao.UserDao;
import team.beatcode.user.entity.User_info;
import team.beatcode.user.entity.User_auth;
import team.beatcode.user.repository.User_infoRepository;
import team.beatcode.user.repository.User_authRepository;

import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    User_infoRepository user_infoRepository;

    User_authRepository user_authRepository;

    // 注入
    public UserDaoImpl(User_infoRepository user_infoRepository, User_authRepository user_authRepository) {
        this.user_infoRepository = user_infoRepository;
        this.user_authRepository = user_authRepository;
    }

    @Override
    public User_info getUser_info(Integer id){
        Optional<User_info> user_info = user_infoRepository.findUser_infoByUserId(id);
        return user_info.orElse(null);
    }

    @Override
    public User_auth getUser_auth(Integer id){
        Optional<User_auth> user_auth = user_authRepository.findUser_authByUserId(id);
        return user_auth.orElse(null);
    }

}
