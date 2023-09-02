package team.beatcode.user.controller;

import sjtu.reins.web.utils.Message;
import team.beatcode.user.entity.Person_info;
import team.beatcode.user.entity.User;
import team.beatcode.user.entity.User_record;
import team.beatcode.user.feign.AuthFeign;
import team.beatcode.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.beatcode.user.utils.MsgEnum;

import java.util.List;
import java.util.Map;

import static team.beatcode.user.utils.Macros.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthFeign authFeign;

    @RequestMapping("/user")
    public User getUser(@RequestBody Map<String, Object> data) {
        Integer userId = (Integer) data.get(USER_CONTEXT_ID);

        return userService.getUser(userId);
    }

    @RequestMapping("/register")
    public void register(@RequestBody Map<String, Object> data) {
        Integer userId = (Integer) data.get("user_id");
        String userName = (String) data.get(USER_CONTEXT_NAME);
        String email = (String) data.get(USER_CONTEXT_EMAIL);
        String phone = (String) data.get(USER_CONTEXT_PHONE);

        userService.register(userId, userName, email, phone);
    }

    @RequestMapping("/getUserInfo")
    public Person_info getUserInfo(@RequestBody Map<String, Object> data) {
        Integer userId = (Integer) data.get(USER_CONTEXT_ID);

        return userService.getUserInfo(userId);
    }

    @RequestMapping("/updateUserName")
    public void updateUserName(@RequestBody Map<String, Object> data) {
        Integer userId = (Integer) data.get(USER_CONTEXT_ID);
        String userName = (String) data.get(USER_CONTEXT_NAME);

        userService.updateUserName(userId, userName);
    }

    @RequestMapping("/updatePassWord")
    public void updatePassWord(@RequestBody Map<String, Object> data) {
        Integer userId = (Integer) data.get(USER_CONTEXT_ID);
        String password = (String) data.get("password");

        userService.updatePassword(userId, password);
    }

    @RequestMapping("/updatePhone")
    public void updatePhone(@RequestBody Map<String, Object> data) {
        Integer userId = (Integer) data.get(USER_CONTEXT_ID);
        String phone = (String) data.get(USER_CONTEXT_PHONE);

        userService.updatePhone(userId, phone);
    }

    @RequestMapping("/checkEmailExist")
    public Boolean checkEmailExist(@RequestBody Map<String, Object> data) {
        String email = (String) data.get(USER_CONTEXT_EMAIL);

        return userService.checkEmailExist(email);
    }

    /**
     * 修改邮箱
     * @param data 传入的数据
     *            data中包含的数据：
     *             userId: 用户id
     *             email: 新邮箱
     *             code: 验证码
     * @return Message 信息
     * 1. 需要验证邮箱是否已经被注册
     * 2. 需要验证验证码是否正确
     * */
    @RequestMapping("/updateEmail")
    public Message updateEmail(@RequestBody Map<String, Object> data) {
        Integer userId = (Integer) data.get(USER_CONTEXT_ID);
        String email = (String) data.get(USER_CONTEXT_EMAIL);
        String code = (String) data.get(USER_CONTEXT_CODE);

        // 验证邮箱是否已经被注册
        if (userService.checkEmailExist(email)) {
            return new Message(MsgEnum.EMAIL_EXIST_FAULT);
        }

        // 调用鉴权服务，验证验证码是否正确
        // ret: 0-验证码正确，1-验证码错误，2-验证码过期
        Integer codeResult = authFeign.checkCode(data);


        if (codeResult == 0) {
            userService.updateEmail(userId, email);
            return new Message(MsgEnum.SUCCESS);
        } else if (codeResult == 1) {
            return new Message(MsgEnum.CODE_ERROR);
        } else {
            return new Message(MsgEnum.CODE_EXPIRED);
        }
    }
}

