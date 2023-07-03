package team.beatcode.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.beatcode.auth.dao.IpAuthDao;
import team.beatcode.auth.dao.UserAuthDao;
import team.beatcode.auth.entity.IpAuth;
import team.beatcode.auth.entity.UserAuth;
import team.beatcode.auth.utils.IpInHttp;
import team.beatcode.auth.utils.Macros;

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
    private String checkAuth(int code) {
        String ip_str = IpInHttp.getIpAddr(request);
        // 无法解析的
        if (ip_str == null) return Macros.AUTH_CHECK_ERROR;

        IpAuth ipAuth = ipAuthDao.getByIp(ip_str);
        // 新使用的ip
        if (ipAuth == null) return Macros.AUTH_CHECK_FAIL;

        long currentTime = System.currentTimeMillis();
        // 过期了，没有续命或者续命了太多次
        if (currentTime - Macros.AUTH_LIFE > ipAuth.getLastFresh() ||
            Macros.AUTH_MAX_REFRESH < ipAuth.getLastFresh() - ipAuth.getLastLogin())
            return Macros.AUTH_CHECK_FAIL;

        UserAuth userAuth = userAuthDao.getUserAuthById(ipAuth.getUserId());
        if (userAuth == null) {
            System.out.printf("USER ABSENT IN IP_AUTH: IP = %s\n", ip_str);
            return Macros.AUTH_CHECK_FAIL;
        }

        // 身份
        if (userAuth.getRole() == code) {
            // 成功，Refresh
            ipAuth.setLastFresh(currentTime);
            ipAuthDao.save(ipAuth);
            return Macros.AUTH_CHECK_SUCCESS;
        } else return Macros.AUTH_CHECK_FAIL;


    }

    @RequestMapping("/CheckUser")
    public String checkUser() {
        return checkAuth(Macros.AUTH_CODE_USER);
    }
    @RequestMapping("/CheckAdmin")
    public String checkAdmin() {
        return checkAuth(Macros.AUTH_CODE_ADMIN);
    }
}
