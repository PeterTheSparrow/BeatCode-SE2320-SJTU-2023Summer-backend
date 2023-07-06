package team.beatcode.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sjtu.reins.web.utils.Message;
import team.beatcode.auth.dao.IpAuthDao;
import team.beatcode.auth.dao.UserAuthDao;
import team.beatcode.auth.entity.IpAuth;
import team.beatcode.auth.entity.UserAuth;
import team.beatcode.auth.feign.UserFeign;
import team.beatcode.auth.utils.ip.IpInHttp;
import team.beatcode.auth.utils.ip.IpInStr;
import team.beatcode.auth.utils.Macros;
import team.beatcode.auth.utils.msg.MessageEnum;
import net.sf.json.JSONObject;

import java.util.Map;

@RestController
public class LogController {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private IpAuthDao ipAuthDao;
    @Autowired
    private UserAuthDao userAuthDao;

    private void saveLogin(String ip, int uid) {
        long currentTime = System.currentTimeMillis();
        IpAuth ipAuth = new IpAuth();
        ipAuth.setIpAddr(IpInStr.ipAddrStrToHex(ip));
        ipAuth.setUserId(uid);
        ipAuth.setLastLogin(currentTime);
        ipAuth.setLastFresh(currentTime);
        ipAuthDao.save(ipAuth);
    }

    /**
     * @param map 参数：<br/>
     *            {<br/>
     *            &emsp;&emsp;"name": string, // 用户名<br/>
     *            &emsp;&emsp;"pass": string  // 密码<br/>
     *            }<br/>
     * @return bookstore经典Message格式，不含数据
     */
    @RequestMapping("/Login")
    public Message login(@RequestBody Map<String, Object> map) {
        try {
            String name = map.get("name").toString();
            String pass = map.get("pass").toString();

            String ip_str = IpInHttp.getIpAddr(request);
            // 无法解析的
            if (ip_str == null)
                return new Message(MessageEnum.IP_FAULT);

            // 检查密码
            UserAuth auth = userAuthDao.getUserAuthByName(name);
            if (auth == null)
                return new Message(MessageEnum.USER_NOT_FOUND_FAULT);
            if (!auth.getPass().equals(pass))
                return new Message(MessageEnum.USER_BAD_PASS_FAIL);

            // 记录
            saveLogin(ip_str, auth.getId());


            // 检查是否是管理员
            if (auth.getRole() == 0)
            {
                // 生成json，作为返回data
                JSONObject json = new JSONObject();
                json.put("is_admin", 0);
                return new Message(MessageEnum.SUCCESS, json);
            }
            else
            {
                JSONObject json = new JSONObject();
                json.put("is_admin", 1);
                return new Message(MessageEnum.SUCCESS, json);
            }
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
        String ip_str = IpInHttp.getIpAddr(request);
        // 无法解析的
        if (ip_str == null)
            return new Message(MessageEnum.IP_FAULT);

        IpAuth auth = ipAuthDao.getByIp(ip_str);
        if (auth != null) {
            // 没登录过也可以登出
            // 手动过期
            auth.setLastFresh(0);
            ipAuthDao.save(auth);
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

            String ip_str = IpInHttp.getIpAddr(request);
            // 无法解析的
            if (ip_str == null)
                return new Message(MessageEnum.IP_FAULT);

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
            saveLogin(ip_str, id);

            // 调用user服务记录更多信息
            map.remove(KEY_PASSWORD);
            map.put("user_id", id);
            userFeign.registerUser(map);

            return new Message(MessageEnum.SUCCESS);
        } catch (NullPointerException e) {
            // 缺少参数
            return new Message(MessageEnum.PARAM_FAIL);
        }
    }
}
