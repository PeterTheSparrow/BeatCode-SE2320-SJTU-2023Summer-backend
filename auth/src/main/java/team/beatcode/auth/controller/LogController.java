package team.beatcode.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.beatcode.auth.dao.IpAuthDao;
import team.beatcode.auth.dao.UserAuthDao;
import team.beatcode.auth.entity.IpAuth;
import team.beatcode.auth.entity.UserAuth;
import team.beatcode.auth.utils.IpInHttp;
import team.beatcode.auth.utils.IpInStr;
import team.beatcode.auth.utils.msg.Message;
import team.beatcode.auth.utils.msg.MessageEnum;

import java.util.Map;

@RestController
public class LogController {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private IpAuthDao ipAuthDao;
    @Autowired
    private UserAuthDao userAuthDao;

    /**
     *
     * @param map 参数：<br/>
     *            {<br/>
     *            &emsp;&emsp;"name": string, // 用户名<br/>
     *            &emsp;&emsp;"pass": string  // 密码<br/>
     *            }<br/>
     * @return bookstore经典Message格式
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

            System.out.println(ip_str);

            // 检查密码
            UserAuth auth = userAuthDao.getUserAuthByName(name);
            if (auth == null)
                return new Message(MessageEnum.USER_NOT_FOUND_FAULT);
            if (!auth.getPass().equals(pass))
                return new Message(MessageEnum.USER_BAD_PASS_FAIL);

            // 记录
            long currentTime = System.currentTimeMillis();
            IpAuth ipAuth = new IpAuth();
            ipAuth.setIpAddr(IpInStr.ipAddrStrToHex(ip_str));
            ipAuth.setUserId(auth.getId());
            ipAuth.setLastLogin(currentTime);
            ipAuth.setLastFresh(currentTime);
            ipAuthDao.save(ipAuth);

            return new Message(MessageEnum.SUCCESS);
        } catch (NullPointerException e) {
            // 缺少参数
            return new Message(MessageEnum.PARAM_FAIL);
        }
    }

    @RequestMapping("/Logout")
    public Message logout() {
        String ip_str = IpInHttp.getIpAddr(request);
        // 无法解析的
        if (ip_str == null)
            return new Message(MessageEnum.IP_FAULT);

        IpAuth auth = ipAuthDao.getByIp(ip_str);
        // 手动过期
        auth.setLastFresh(0);
        ipAuthDao.save(auth);

        return new Message(MessageEnum.SUCCESS);
    }

    @RequestMapping("/Register")
    public String register() {
        // TODO
        return null;
    }
}
