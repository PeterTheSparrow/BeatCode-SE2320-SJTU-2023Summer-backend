package team.beatcode.consumer.interceptors;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import sjtu.reins.web.utils.Message;
import team.beatcode.consumer.feign.AuthFeign;
import team.beatcode.consumer.utils.Macros;
import team.beatcode.consumer.utils.context.UserContext;
import team.beatcode.consumer.utils.context.UserContextHolder;
import team.beatcode.consumer.utils.msg.MessageEnum;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

@AllArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {
    private AuthFeign authFeign;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(@Nullable HttpServletRequest request,
                             @Nullable HttpServletResponse response,
                             @Nullable Object handler) {
        /*
          鸣谢:
            https://zhuanlan.zhihu.com/p/124030429
            https://www.cnblogs.com/funmans/p/10003130.html
         */

        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }
        Method method = handlerMethod.getMethod();
        // 判断接口是否需要登录
        RequireLogin methodAnnotation = method.getAnnotation(RequireLogin.class);
        // 有 @RequireLogin 注解，需要认证
        if (methodAnnotation != null) {
            // 这写你拦截需要干的事儿，比如取缓存，SESSION，权限判断等
            // 远程请求
            Message rt =
                methodAnnotation.type() == RequireLogin.Type.ADMIN ? authFeign.checkAdmin() :
                methodAnnotation.type() == RequireLogin.Type.USER ? authFeign.checkUser() :
                null;
            // auth爆了
            if (rt == null || rt.getStatus() == MessageEnum.AUTH_AUTH_ERROR.getStatus()) {
                System.out.println("Auth-app Boom!");
            }
            // 鉴权成功，放行
            else if (rt.getStatus() == MessageEnum.AUTH_AUTH_SUCCESS.getStatus()) {
                try {
                    UserContext context = objectMapper.convertValue(
                            rt.getData(), UserContext.class);
                    UserContextHolder.setUserAccount(context);
                }
                catch (IllegalArgumentException e) {
                    System.out.printf("Conflict Message: %s\n", rt);
                    e.printStackTrace();
                }
                return true;
            }
            // 鉴权失败，拦截并返回”未登录“
            else if (rt.getStatus() == MessageEnum.AUTH_AUTH_FAIL.getStatus()) {
                if (response != null) {
                    // 添加必要的Header
                    Macros.implResponse(response);
                    // 回敬一个失败
                    try (PrintWriter writer = response.getWriter()) {
                        writer.print(new Message(MessageEnum.AUTH_FAIL,
                                methodAnnotation.type() == RequireLogin.Type.ADMIN ? 1 : 0));
                    } catch (IOException e) {
                        System.out.println("IOE in Admin Interceptor");
                    }
                }
                return false;
            }
            return false;
        }
        // 没有注解则跳过
        else return true;
    }

    @Override
    public void afterCompletion(@Nullable HttpServletRequest request,
                                @Nullable HttpServletResponse response,
                                @Nullable Object handler,
                                @Nullable Exception ex) {
        UserContextHolder.clear();
    }
}
