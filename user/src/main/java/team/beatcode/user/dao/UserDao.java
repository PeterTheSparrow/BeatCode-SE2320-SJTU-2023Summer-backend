package team.beatcode.user.dao;

import team.beatcode.user.entity.User_auth;
import team.beatcode.user.entity.User_info;
import team.beatcode.user.entity.User_record;

public interface UserDao {
    User_info getUser_info(Integer userId);

//    User_auth getUser_auth(Integer userId);
    User_record getUser_record(Integer userId);

}
