package team.beatcode.user.dao;

import team.beatcode.user.entity.User_auth;
import team.beatcode.user.entity.User_info;

public interface UserDao {
    User_info getUser_info(Integer id);

    User_auth getUser_auth(Integer id);

}
