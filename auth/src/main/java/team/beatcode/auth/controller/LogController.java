package team.beatcode.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sjtu.reins.web.utils.Message;
import team.beatcode.auth.Service.CodeService;
import team.beatcode.auth.dao.TokenAuthDao;
import team.beatcode.auth.dao.UserAuthDao;
import team.beatcode.auth.entity.TokenAuth;
import team.beatcode.auth.entity.UserAuth;
import team.beatcode.auth.feign.UserFeign;
import team.beatcode.auth.utils.Macros;
import team.beatcode.auth.utils.TokenUtils;
import team.beatcode.auth.utils.msg.MessageEnum;

import java.util.Map;
import java.util.TreeMap;

@RestController
public class LogController {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private TokenAuthDao tokenAuthDao;
    @Autowired
    private UserAuthDao userAuthDao;

    @Autowired
    private CodeService codeService;

    private void saveLogin(byte[] token, int uid) {
        long currentTime = System.currentTimeMillis();
        TokenAuth tokenAuth = new TokenAuth();
        tokenAuth.setToken(token);
        tokenAuth.setUserId(uid);
        tokenAuth.setLastLogin(currentTime);
        tokenAuth.setLastFresh(currentTime);
        tokenAuthDao.save(tokenAuth);
    }

    /**
     * @param map 参数：<br/>
     *            {<br/>
     *            &emsp;&emsp;"name": string, // 用户名<br/>
     *            &emsp;&emsp;"pass": string, // 密码<br/>
     *            &emsp;&emsp;"data": { // 额外信息<br/>
     *            &emsp;&emsp;&emsp;&emsp;"golden-class-token": string, // Token(base64)，不要动，不用改<br/>
     *            &emsp;&emsp;&emsp;&emsp;"is_admin": number // role<br/>
     *            &emsp;&emsp;}<br/>
     *            }<br/>
     * @return bookstore经典Message格式，不含数据
     */
    @RequestMapping("/Login")
    public Message login(@RequestBody Map<String, Object> map) {
        try {
            String name = map.get("name").toString();
            String pass = map.get("pass").toString();

            // 检查密码
            UserAuth auth = userAuthDao.getUserAuthByName(name);
            if (auth == null)
                return new Message(MessageEnum.USER_NOT_FOUND_FAULT);
            if (!auth.getPass().equals(pass))
                return new Message(MessageEnum.USER_BAD_PASS_FAIL);

            // 记录
            byte[] token = TokenUtils.generate();
            saveLogin(token, auth.getId());

            Map<String, Object> data = new TreeMap<>();
            // 检查是否是管理员
            data.put(Macros.IS_ADMIN, auth.getRole());
            // 放入刚刚生成的Token
            data.put(Macros.TOKEN_NAME, TokenUtils.BytesToString(token));
            return new Message(MessageEnum.SUCCESS, data);
        } catch (NullPointerException e) {
            // 缺少参数
            return new Message(MessageEnum.PARAM_FAIL);
        }
    }

    /**
     * @return bookstore经典Message格式，不含数据
     */
    @RequestMapping("/Logout")
    public Message logout() {
        String token = request.getHeader(Macros.TOKEN_NAME);
        if (token == null)
            return new Message(MessageEnum.TOKEN_FAULT);

        byte[] bytes = TokenUtils.StringToBytes(token);
        if (bytes == null)
            return new Message(MessageEnum.TOKEN_FAULT);

        TokenAuth auth = tokenAuthDao.getByTokenBytes(bytes);
        if (auth != null) {
            // 没登录过也可以登出
            tokenAuthDao.remove(token);
        }

        return new Message(MessageEnum.SUCCESS);
    }

    /**
     * @param map 参数：<br/>
     *            {<br/>
     *            &emsp;&emsp;"name": string, // 用户名<br/>
     *            &emsp;&emsp;"pass": string,  // 密码<br/>
     *            &emsp;&emsp;"email": string,  //邮箱<br/>
     *            &emsp;&emsp;"phone": string,  //电话<br/>
     *            }<br/>
     * @return bookstore经典Message格式，不含数据
     */
    @RequestMapping("/Register")
    public Message register(@RequestBody Map<String, Object> map) {
        try {
            String KEY_PASSWORD = "pass";
            String name = map.get("name").toString();
            String pass = map.get(KEY_PASSWORD).toString();

            // 检查用户名是否重复
            UserAuth checkExistAuth = userAuthDao.getUserAuthByName(name);

            if (checkExistAuth != null)
                return new Message(MessageEnum.USER_EXIST_FAULT);

            // 检查用户邮箱验证码
            String email = map.get("email").toString();
            String code = map.get("code").toString();

            Integer returnVal = codeService.checkCode(email, code);

            if (returnVal == 1) {
                // 验证码错误
                return new Message(MessageEnum.VERIFICATION_CODE_ERROR);
            }
            else if (returnVal == 2) {
                // 验证码过期
                return new Message(MessageEnum.VERIFICATION_CODE_EXPIRED);
            }


            // 生成新用户
            UserAuth auth = new UserAuth();
            auth.setName(name);
            auth.setPass(pass);
            auth.setRole(Macros.AUTH_CODE_USER);
            auth = userAuthDao.save(auth);

            // 记录
            Integer id = auth.getId();
            byte[] token = TokenUtils.generate();
            saveLogin(token, id);

            // 调用user服务记录更多信息
            map.remove(KEY_PASSWORD);
            map.put("user_id", id);
            userFeign.registerUser(map);

            Map<String, Object> data = new TreeMap<>();
            // 不是管理员
            data.put(Macros.IS_ADMIN, Macros.AUTH_CODE_USER);
            // 放入刚刚生成的Token
            data.put(Macros.TOKEN_NAME, TokenUtils.BytesToString(token));
            return new Message(MessageEnum.SUCCESS, data);
        } catch (NullPointerException e) {
            // 缺少参数
            return new Message(MessageEnum.PARAM_FAIL);
        }
    }


    /**
     * 修改用户名
     * @param map 传入的数据
     *             data中包含的数据：
     *            user_id: 用户id
     *            user_name: 新用户名
     * @return Boolean 是否成功
     * */
    @RequestMapping("/updateUserNameForAuth")
    public Boolean updateUserNameForAuth(@RequestBody Map<String, Object> map) {
        Integer userId = (Integer) map.get(Macros.USER_CONTEXT_ID);
        String userName = (String) map.get(Macros.USER_CONTEXT_NAME);

        // 检查参数
        if (userId == null || userName == null) {
            return false;
        }

        // 更新user_auth表，成功更新返回成功信息，否则返回失败信息
        UserAuth userAuth = userAuthDao.getUserAuthById(userId);


        if (userAuth != null) {
            userAuth.setName(userName);
            System.out.println(userAuth);
            userAuthDao.save(userAuth);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 修改密码
     * @param map 传入的数据
     *             data中包含的数据：
     *            user_id: 用户id
     *            password: 新密码
     * @return Boolean 是否成功
     * */
    @RequestMapping("/updatePassWordForAuth")
    public Boolean updatePassWordForAuth(@RequestBody Map<String, Object> map) {
        Integer userId = (Integer) map.get(Macros.USER_CONTEXT_ID);
        String password = (String) map.get("password");

        // 更新user_auth表，成功更新返回成功信息，否则返回失败信息
        // 先调用Dao查找用户，找到以后就更新，调用Dao来save；否则返回失败信息
        UserAuth userAuth = userAuthDao.getUserAuthById(userId);

        if (userAuth != null) {
            userAuth.setPass(password);
            userAuthDao.save(userAuth);
            return true;
        }
        else {
            return false;
        }

    }

    @RequestMapping("/getPassword")
    public String getPassword(@RequestBody Map<String, Object> map) {
        Integer userId = (Integer) map.get(Macros.USER_CONTEXT_ID);

        UserAuth userAuth = userAuthDao.getUserAuthById(userId);

        if (userAuth != null) {
            return userAuth.getPass();
        }
        else {
            return null;
        }
    }

}
