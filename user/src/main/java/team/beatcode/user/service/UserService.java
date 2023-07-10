package team.beatcode.user.service;

import team.beatcode.user.entity.User;
import team.beatcode.user.entity.User_record;

import java.util.List;

public interface UserService {
    User getUser(Integer userId);

    void register(Integer userId, String userName, String email, String phone);

    List<User_record> getRanks();

}
