package team.beatcode.user.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import team.beatcode.user.dao.UserDao;
import team.beatcode.user.entity.Person_info;
import team.beatcode.user.entity.User_info;
import team.beatcode.user.entity.User_record;
import team.beatcode.user.entity.User_auth;
import team.beatcode.user.repository.User_infoRepository;
import team.beatcode.user.repository.User_recordRepository;
import team.beatcode.user.repository.User_authRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    User_infoRepository user_infoRepository;

    @Autowired
    User_recordRepository user_recordRepository;

    @Autowired
    User_authRepository user_authRepository;

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

    @Override
    public Boolean checkUserExist(Integer userId) {
        Optional<User_info> user_info = user_infoRepository.findUser_infoByUserId(userId);
        return user_info.isPresent();
    }

    @Override
    public Person_info getUserInfo(Integer userId) {
        Optional<User_auth> user_auth = user_authRepository.findUser_authByUserId(userId);
        if(user_auth.isPresent()){
            Optional<User_info> user_info = user_infoRepository.findUser_infoByUserId(userId);
            User_auth user_auth1 = user_auth.get();
            User_info user_info1 = user_info.get();
            Person_info person_info = new Person_info();
            person_info.setUserName(user_info1.getUserName());
            person_info.setEmail(user_info1.getEmail());
            person_info.setPassword(user_auth1.getPassword());
            person_info.setPhone(user_info1.getPhone());
            return person_info;
        }
        else {
            System.out.println("User not found");
            return null;
        }
    }

    @Override
    public void updateUserName(Integer userId, String userName) {
        Optional<User_info> user_info = user_infoRepository.findUser_infoByUserId(userId);

        if(user_info.isPresent()){
            User_info user_info1 = user_info.get();
            user_info1.setUserName(userName);
            user_infoRepository.save(user_info1);
        }
        else {
            System.out.println("User not found");
        }
    }

    @Override
    public void updatePassword(Integer userId, String password) {
        Optional<User_auth> user_auth = user_authRepository.findUser_authByUserId(userId);

        if(user_auth.isPresent()){
            User_auth user_auth1 = user_auth.get();
            user_auth1.setPassword(password);
            user_authRepository.save(user_auth1);
        }
        else {
            System.out.println("User not found");
        }
    }

    @Override
    public void updatePhone(Integer userId, String phone) {
        Optional<User_info> user_info = user_infoRepository.findUser_infoByUserId(userId);

        if(user_info.isPresent()){
            User_info user_info1 = user_info.get();
            user_info1.setPhone(phone);
            user_infoRepository.save(user_info1);
        }
        else {
            System.out.println("User not found");
        }
    }

    @Override
    public Boolean checkEmailExist(String email) {
        Optional<User_info> user_info = user_infoRepository.findUser_infoByEmail(email);
        return user_info.isPresent();
    }

    @Override
    public void updateEmail(Integer userId, String email) {
        Optional<User_info> user_info = user_infoRepository.findUser_infoByUserId(userId);

        if(user_info.isPresent()){
            User_info user_info1 = user_info.get();
            user_info1.setEmail(email);
            user_infoRepository.save(user_info1);
        }
        else {
            System.out.println("User not found");
        }
    }
}
