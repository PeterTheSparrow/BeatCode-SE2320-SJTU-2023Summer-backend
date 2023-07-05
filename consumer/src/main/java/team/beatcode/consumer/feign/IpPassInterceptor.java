package team.beatcode.consumer.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import team.beatcode.consumer.utils.Macros;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IpPassInterceptor implements RequestInterceptor {

    private static void headerRepeater(String header_key,
                                       RequestTemplate template,
                                       HttpServletRequest request) {
        String ip = request.getHeader(header_key);
        if (ip != null) template.header(header_key, ip);
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();

            headerRepeater(Macros.X_REAL_IP, requestTemplate, request);
            headerRepeater(Macros.PROXY_CLIENT_IP, requestTemplate, request);
            headerRepeater(Macros.WL_PROXY_CLIENT_IP, requestTemplate, request);

            String xff = request.getHeader(Macros.X_FORWARDED_FOR);
            try {
                String myNetIp = InetAddress.getLocalHost().getHostAddress();
                requestTemplate.header(Macros.X_FORWARDED_FOR,
                        // if no xff, then RemoteAddress
                        (xff == null ? request.getRemoteAddr() : xff) + ',' + myNetIp);
            } catch (UnknownHostException e) {
                requestTemplate.header(Macros.X_FORWARDED_FOR,
                        (xff == null ? request.getRemoteAddr() : xff));
                throw new RuntimeException(e);
            }
        }
    }
}
