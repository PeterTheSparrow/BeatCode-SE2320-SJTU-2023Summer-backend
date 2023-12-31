package team.beatcode.consumer.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import team.beatcode.consumer.utils.Macros;
import team.beatcode.consumer.utils.context.UserContextHolder;

public class TokenPassInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String tokenInHolder = UserContextHolder.getToken();
            if (tokenInHolder != null)
                requestTemplate.header(Macros.TOKEN_NAME, tokenInHolder);
            else {
                String token = request.getHeader(Macros.TOKEN_NAME);
                if (token != null)
                    requestTemplate.header(Macros.TOKEN_NAME, token);
            }
        }
    }
}
