package team.beatcode.user.controller;

import sjtu.reins.web.utils.Message;
import team.beatcode.user.entity.*;
import team.beatcode.user.feign.AuthFeign;
import team.beatcode.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.beatcode.user.utils.MsgEnum;

import java.util.HashMap;
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

    @RequestMapping("/ranks")
    public List<User_record> getRanks() {
        return userService.getRanks();
    }

    @RequestMapping("/checkUserExist")
    public Boolean checkUserExist(@RequestBody Map<String, Object> data) {
        Integer userId = (Integer) data.get(USER_CONTEXT_ID);

        return userService.checkUserExist(userId);
    }

    @RequestMapping("/getUserInfo")
    public Person_info getUserInfo(@RequestBody Map<String, Object> data) {
        Integer userId = (Integer) data.get(USER_CONTEXT_ID);

        Person_info personInfo = userService.getUserInfo(userId);

        // set map
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("password", data.get("password"));

        // get password
        String password = authFeign.getPassword(map);

        if (password == null) {
            System.out.println("password is null");
            return null;
        }

        // set password
        personInfo.setPassword(password);

        return personInfo;
    }

    /**
     * 修改用户名
     * @param data 传入的数据
     *            data中包含的数据：
     *             userId: 用户id
     *             name: 新用户名
     * @return Message 信息
     * 1. 需要验证用户名是否已经被注册
     * 2. 需要调用鉴权微服务来修改用户名
     * */
    @RequestMapping("/updateUserName")
    public Message updateUserName(@RequestBody Map<String, Object> data) {
        Integer userId = (Integer) data.get(USER_CONTEXT_ID);
        String userName = (String) data.get(USER_CONTEXT_NAME);

        // 判断解析参数错误
        if (userId == null || userName == null) {
            return new Message(MsgEnum.PARAM_FAIL);
        }

        // 检查用户名是否重复
        if (userService.checkUserNameExist(userName)) {
            return new Message(MsgEnum.USER_NAME_EXIST_FAULT);
        }

        /*
        * 打包数据：
        * user_id: 用户id
        * user_name: 新用户名
        * */
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("user_name", userName);

        // 调用鉴权微服务来修改用户名
        if (authFeign.updateUserNameForAuth(map)) {
            userService.updateUserName(userId, userName);
            return new Message(MsgEnum.SUCCESS);
        } else {
            return new Message(MsgEnum.FAIL);
        }
    }

    /**
     * 修改密码
     * @param data 传入的数据
     *            data中包含的数据：
     *             userId: 用户id
     *             password: 新密码
     * @return Message 信息
     * 1. 需要调用鉴权微服务来修改密码
     * */
    @RequestMapping("/updatePassWord")
    public Message updatePassWord(@RequestBody Map<String, Object> data) {
        Integer userId = (Integer) data.get(USER_CONTEXT_ID);
        String password = (String) data.get("password");

        if (userId == null || password == null) {
            return new Message(MsgEnum.PARAM_FAIL);
        }

        // 调用鉴权微服务来修改密码
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("password", password);

        if (authFeign.updatePassWordForAuth(map)) {
            return new Message(MsgEnum.SUCCESS);
        } else {
            return new Message(MsgEnum.FAIL);
        }
    }

    /**
     * 修改手机号
     * @param data 传入的数据
     *             data中包含的数据：
     *             userId: 用户id
     *             phone: 新手机号
     * @return Message 信息
     * */
    @RequestMapping("/updatePhone")
    public Message updatePhone(@RequestBody Map<String, Object> data) {
        Integer userId = (Integer) data.get(USER_CONTEXT_ID);
        String phone = (String) data.get(USER_CONTEXT_PHONE);

        if (userId == null || phone == null) {
            return new Message(MsgEnum.PARAM_FAIL);
        }

        if(userService.checkUserExist(userId)){
            userService.updatePhone(userId, phone);
            return new Message(MsgEnum.SUCCESS);
        }
        else{
            return new Message(MsgEnum.USER_NOT_EXIST);
        }
    }

    @RequestMapping("/getProblemList")
    public Message getProblemList(@RequestBody Map<String, Object> map) {
        try{
            System.out.println("breakpoint0");
            Integer pageIndex = (Integer) map.get(PAGE_INDEX);
            System.out.println("breakpoint1, pageIndex = " + pageIndex);
            Integer pageSize = (Integer) map.get(PAGE_SIZE);
            System.out.println("breakpoint2, pageSize = " + pageSize);
            String userId = (String) map.get(USER_CONTEXT_ID);
            System.out.println("breakpoint3, userId = " + userId);
            if (pageIndex == null || pageSize == null)
                return new Message(MsgEnum.FAIL);

            System.out.println("breakpoint4");

            if (pageIndex <= 0)
                return new Message(MsgEnum.PAGE_NEGATIVE);

            System.out.println("breakpoint5");

            if (pageSize <= 1)
                return new Message(MsgEnum.PAGE_MALICE);

            System.out.println("breakpoint6");

            UserCondition userCondition = userService.getUserCondition(userId);

            System.out.println("breakpoint7");

            String problemCondition = userCondition.getProblemCondition();

            System.out.println("breakpoint8");

            User_problem.Paged result = userService.getProblemList(pageIndex-1, pageSize, problemCondition);

            System.out.println("breakpoint9");

            return new Message(MsgEnum.SUCCESS, result);
        }
        catch (Exception e){
            return new Message(MsgEnum.FAIL);
        }
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

        if (userId == null || email == null || code == null) {
            return new Message(MsgEnum.PARAM_FAIL);
        }

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

