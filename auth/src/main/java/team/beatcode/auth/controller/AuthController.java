package team.beatcode.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sjtu.reins.web.utils.Message;
import team.beatcode.auth.dao.TokenAuthDao;
import team.beatcode.auth.dao.UserAuthDao;
import team.beatcode.auth.entity.TokenAuth;
import team.beatcode.auth.entity.UserAuth;
import team.beatcode.auth.utils.Macros;
import team.beatcode.auth.utils.msg.MessageEnum;

import java.util.Map;
import java.util.TreeMap;

@RestController
public class AuthController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private TokenAuthDao tokenAuthDao;
    @Autowired
    private UserAuthDao userAuthDao;

    /**
     * @return - 成功："yes" <br/>
     *         - 失败："no" <br/>
     *         - 无法解析ip："wtf" <br/>
     */
    private Message checkAuth(int code) {
        String tokenStr = request.getHeader(Macros.TOKEN_NAME);
        if (tokenStr == null) return new Message(MessageEnum.AUTH_ERROR);

        TokenAuth tokenAuth = tokenAuthDao.getByToken(tokenStr);
        // 不存在的TOKEN
        if (tokenAuth == null)
            return new Message(MessageEnum.AUTH_FAIL);

        long currentTime = System.currentTimeMillis();
        // 过期了，没有续命或者续命了太多次
        if (currentTime - Macros.AUTH_LIFE > tokenAuth.getLastFresh() ||
            Macros.AUTH_MAX_REFRESH < tokenAuth.getLastFresh() - tokenAuth.getLastLogin())
            return new Message(MessageEnum.AUTH_FAIL);

        UserAuth userAuth = userAuthDao.getUserAuthById(tokenAuth.getUserId());
        if (userAuth == null) {
            System.out.printf("USER ABSENT IN TOKEN_AUTH: TOKEN = %s\n", tokenStr);
            return new Message(MessageEnum.AUTH_ERROR);
        }

        // 身份
        if (userAuth.getRole() == code) {
            // 成功，Refresh
            tokenAuth.setLastFresh(currentTime);
            tokenAuthDao.save(tokenAuth);
            System.out.println(tokenAuth.getUserId());
            // 返回USER上下文
            Map<String, Object> map = new TreeMap<>();
            map.put(Macros.USER_CONTEXT_ID, userAuth.getId());
            map.put(Macros.USER_CONTEXT_NAME, userAuth.getName());
            map.put(Macros.USER_CONTEXT_ROLE, userAuth.getRole());
            return new Message(MessageEnum.AUTH_SUCCESS, map);
        }
        else
            return new Message(MessageEnum.AUTH_FAIL);
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
