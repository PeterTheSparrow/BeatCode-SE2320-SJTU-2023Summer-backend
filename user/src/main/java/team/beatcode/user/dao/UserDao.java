package team.beatcode.user.dao;

import team.beatcode.user.entity.Person_info;
import team.beatcode.user.entity.User_info;
import team.beatcode.user.entity.User_record;

import java.util.List;

public interface UserDao {
    User_info getUser_info(Integer userId);

    User_record getUser_record(Integer userId);

    void register(Integer userId, String userName, String email, String phone);

    List<User_record> getRecords();

    Boolean checkUserExist(Integer userId);

    Person_info getUserInfo(Integer userId);

    void updateUserName(Integer userId, String userName);

    void updatePassword(Integer userId, String password);

    void updatePhone(Integer userId, String phone);

    Boolean checkEmailExist(String email);

    void updateEmail(Integer userId, String email);
}
