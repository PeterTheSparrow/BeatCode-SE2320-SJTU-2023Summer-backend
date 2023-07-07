package team.beatcode.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sjtu.reins.web.utils.Message;
import team.beatcode.auth.dao.IpAuthDao;
import team.beatcode.auth.dao.UserAuthDao;
import team.beatcode.auth.entity.IpAuth;
import team.beatcode.auth.entity.UserAuth;
import team.beatcode.auth.utils.ip.IpInHttp;
import team.beatcode.auth.utils.Macros;
import team.beatcode.auth.utils.msg.MessageEnum;

import java.util.Map;
import java.util.TreeMap;

@RestController
public class AuthController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private IpAuthDao ipAuthDao;
    @Autowired
    private UserAuthDao userAuthDao;

    /**
     * @return - 成功："yes" <br/>
     *         - 失败："no" <br/>
     *         - 无法解析ip："wtf" <br/>
     */
    private Message checkAuth(int code) {
        String ip_str = IpInHttp.getIpAddr(request);
        // 无法解析的
        if (ip_str == null) return new Message(MessageEnum.AUTH_ERROR);

        IpAuth ipAuth = ipAuthDao.getByIp(ip_str);
        // 新使用的ip
        if (ipAuth == null) return new Message(MessageEnum.AUTH_FAIL);

        long currentTime = System.currentTimeMillis();
        // 过期了，没有续命或者续命了太多次
        if (currentTime - Macros.AUTH_LIFE > ipAuth.getLastFresh() ||
            Macros.AUTH_MAX_REFRESH < ipAuth.getLastFresh() - ipAuth.getLastLogin())
            return new Message(MessageEnum.AUTH_FAIL);

        UserAuth userAuth = userAuthDao.getUserAuthById(ipAuth.getUserId());
        if (userAuth == null) {
            System.out.printf("USER ABSENT IN IP_AUTH: IP = %s\n", ip_str);
            return new Message(MessageEnum.AUTH_FAIL);
        }

        // 身份
        if (userAuth.getRole() == code) {
            // 成功，Refresh
            ipAuth.setLastFresh(currentTime);
            ipAuthDao.save(ipAuth);
            System.out.println(ipAuth.getUserId());
            // 返回USER上下文
            Map<String, Object> map = new TreeMap<>();
            map.put(Macros.USER_CONTEXT_ID, userAuth.getId());
            map.put(Macros.USER_CONTEXT_NAME, userAuth.getName());
            map.put(Macros.USER_CONTEXT_ROLE, userAuth.getRole());
            return new Message(MessageEnum.AUTH_SUCCESS, map);
        } else  return new Message(MessageEnum.AUTH_FAIL);
    }

    @RequestMapping("/CheckUser")
    public Message checkUser() {
        return checkAuth(Macros.AUTH_CODE_USER);
    }
    @RequestMapping("/CheckAdmin")
    public Message checkAdmin() {
        return checkAuth(Macros.AUTH_CODE_ADMIN);
    }
}
