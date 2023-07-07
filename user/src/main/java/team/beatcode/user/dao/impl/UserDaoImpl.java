package team.beatcode.user.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import team.beatcode.user.dao.UserDao;
import team.beatcode.user.entity.User_info;
import team.beatcode.user.entity.User_record;
import team.beatcode.user.repository.User_infoRepository;
import team.beatcode.user.repository.User_recordRepository;

import java.util.List;
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

    @Override
    public User_record getUser_record(Integer userId){
        Optional<User_record> user_record = user_recordRepository.findUser_recordByUserId(userId);
        return user_record.orElse(null);
    }

    @Override
    public void register(Integer userId, String userName, String email, String phone){
        User_info user_info = new User_info();
        user_info.setUserId(userId);
        user_info.setUserName(userName);
        user_info.setEmail(email);
        user_info.setPhone(phone);
        User_record user_record = new User_record();
        user_record.setUserId(userId);
        user_record.setAcceptNum(0);
        user_record.setAcceptSubmit(0);
        user_record.setSubmitNum(0);

        user_infoRepository.save(user_info);
        user_recordRepository.save(user_record);
    }

    @Override
    public List<User_record> getRecords(){
        return user_recordRepository.findAll();
    }


}
