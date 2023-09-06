package team.beatcode.consumer.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
import sjtu.reins.web.utils.Message;
import team.beatcode.consumer.feign.AuthFeign;
import team.beatcode.consumer.utils.context.UserContext;
import team.beatcode.consumer.utils.context.UserContextHolder;
import team.beatcode.consumer.utils.msg.MessageEnum;

import java.util.Map;

public class WebSocketInterceptor extends HttpSessionHandshakeInterceptor {

    @Autowired
    private AuthFeign authFeign;

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public boolean beforeHandshake(@NonNull ServerHttpRequest request,
                                   @NonNull ServerHttpResponse response,
                                   @NonNull WebSocketHandler wsHandler,
                                   @NonNull Map<String, Object> attributes) {
        System.out.printf("Handshake Request: \n\t%s\n", request);
        if (request instanceof ServletServerHttpRequest serverHttpRequest) {
            // 获取参数
            String token = serverHttpRequest.getServletRequest().getParameter("Token");
            if (token != null) {
                UserContextHolder.setToken(token);
                Message rt = authFeign.checkUser();
                UserContextHolder.clearToken();
                if (rt == null || rt.getStatus() == MessageEnum.AUTH_AUTH_ERROR.getStatus()) {
                    System.out.println("Auth-app Boom!");
                    return false;
                }
                else if (rt.getStatus() == MessageEnum.AUTH_AUTH_SUCCESS.getStatus()) {
                    try {
                        UserContext context = mapper.convertValue(rt.getData(), UserContext.class);
                        attributes.put(PushHandler.ATTRIBUTES_USERID_INTEGER, context.getUser_id());
                        return true;
                    }
                    catch (IllegalArgumentException e) {
                        System.out.printf("Conflict Message: %s\n", rt);
                        e.printStackTrace();
                        return false;
                    }
                }
                else
                    return false;
            }
        }

        // 你谁啊，不准连
        return false;
        // https://blog.csdn.net/fffvdgjvbsfkb123456/article/details/116465394
    }

    // 初次握手访问后
    @Override
    public void afterHandshake(@NonNull ServerHttpRequest serverHttpRequest,
                               @NonNull ServerHttpResponse serverHttpResponse,
                               @NonNull WebSocketHandler webSocketHandler,
                               Exception e) {
        System.out.printf("AfterHandshake Request: \n\t%s\n", serverHttpRequest);
    }
}
