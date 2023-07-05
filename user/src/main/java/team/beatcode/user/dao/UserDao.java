package team.beatcode.user.dao;

import team.beatcode.user.entity.User_admin;
import team.beatcode.user.entity.User_info;

import java.util.List;

public interface UserDao {
    User_info getUser_info(Integer id);

    User_admin getUser_admin(Integer id);

}
