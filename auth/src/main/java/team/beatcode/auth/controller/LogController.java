package team.beatcode.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sjtu.reins.web.utils.Message;
import team.beatcode.auth.dao.TokenAuthDao;
import team.beatcode.auth.dao.UserAuthDao;
import team.beatcode.auth.entity.TokenAuth;
import team.beatcode.auth.entity.UserAuth;
import team.beatcode.auth.feign.UserFeign;
import team.beatcode.auth.utils.Macros;
import team.beatcode.auth.utils.UUIDUtils;
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
            byte[] token = UUIDUtils.generate();
            saveLogin(token, auth.getId());

            Map<String, Object> data = new TreeMap<>();
            // 检查是否是管理员
            data.put("is_admin", auth.getRole());
            // 放入刚刚生成的Token
            data.put(Macros.TOKEN_NAME, UUIDUtils.BytesToString(token));
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

        TokenAuth auth = tokenAuthDao.getByToken(token);
        if (auth != null) {
            // 没登录过也可以登出
            // 手动过期
            auth.setLastFresh(0);
            tokenAuthDao.save(auth);
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

            // 生成新用户
            UserAuth auth = new UserAuth();
            auth.setName(name);
            auth.setPass(pass);
            auth.setRole(Macros.AUTH_CODE_USER);
            auth = userAuthDao.save(auth);

            // 记录
            Integer id = auth.getId();
            byte[] token = UUIDUtils.generate();
            saveLogin(token, id);

            // 调用user服务记录更多信息
            map.remove(KEY_PASSWORD);
            map.put("user_id", id);
            userFeign.registerUser(map);

            Map<String, Object> data = new TreeMap<>();
            // 不是管理员
            data.put("is_admin", Macros.AUTH_CODE_USER);
            // 放入刚刚生成的Token
            data.put(Macros.TOKEN_NAME, UUIDUtils.BytesToString(token));
            return new Message(MessageEnum.SUCCESS, data);
        } catch (NullPointerException e) {
            // 缺少参数
            return new Message(MessageEnum.PARAM_FAIL);
        }
    }
}
