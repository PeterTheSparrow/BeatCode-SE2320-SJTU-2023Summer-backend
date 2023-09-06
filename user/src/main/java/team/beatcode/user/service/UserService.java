package team.beatcode.user.service;

import team.beatcode.user.entity.Person_info;
import team.beatcode.user.entity.User;
import team.beatcode.user.entity.User_record;

import java.util.List;

public interface UserService {
    User getUser(Integer userId);

    void register(Integer userId, String userName, String email, String phone);

    List<User_record> getRanks();

    Boolean checkUserExist(Integer userId);

    Person_info getUserInfo(Integer userId);

    Boolean updateUserName(Integer userId, String userName);

    void updatePhone(Integer userId, String phone);

    Boolean checkEmailExist(String email);

    void updateEmail(Integer userId, String email);

    Boolean checkUserNameExist(String userName);
}
