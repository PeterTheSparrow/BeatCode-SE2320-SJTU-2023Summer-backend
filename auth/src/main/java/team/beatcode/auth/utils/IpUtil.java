package team.beatcode.auth.utils;

import jakarta.servlet.http.HttpServletRequest;

import java.net.InetAddress;
import java.net.UnknownHostException;
/*
 * getRemoteAddr()方法返回的是IPv4或IPv6格式的IP地址。
 * 如果你的应用部署在反向代理或负载均衡器后面，那么获取到的可能是反向代理或负载均衡器的IP地址。
 * 如果你需要获取真实的客户端IP地址，可以考虑使用X-Forwarded-For或X-Real-IP等HTTP头字段来获取。
 *
 * X-Forwarded-For（XFF）和X-Real-IP（XRI）都是HTTP头字段，用于获取客户端的真实IP地址。
 * 它们的区别在于：
 *     X-Forwarded-For（XFF）：这个字段是由代理服务器（如反向代理、负载均衡器）添加的。
 *         它的值是一个逗号分隔的IP地址列表，最左边的IP地址是最初的客户端IP地址，
 *         后面的IP地址都是经过的代理服务器的IP地址。在多级代理的情况下，
 *         可以通过解析这个字段来获取到真实的客户端IP地址。
 *     X-Real-IP（XRI）：这个字段是由一些反向代理服务器或Web服务器添加的。
 *         它的值是客户端的真实IP地址，通常是最后一个代理服务器的IP地址。
 *         与X-Forwarded-For不同，它只包含一个IP地址，不是一个列表。
 * 需要注意的是，这些字段的值是由代理服务器或反向代理服务器添加的，因此在某些情况下可能会被伪造或篡改。
 * 所以在使用这些字段时，需要进行适当的验证和处理。
 */
public class IpUtil {
    private static final String UNKNOWN = "unknown";
    private static final String LOCALHOST = "127.0.0.1";

    private static boolean checkIpValid(String ip) {
        return ip != null && ip.length() != 0 && !UNKNOWN.equalsIgnoreCase(ip);
    }

    private static String toSingleIp(String ips) {
        int ind = ips.indexOf(",");
        if (ind > 0) return ips.substring(0, ind);
        else return ips;
    }

    public static String getIpAddr(HttpServletRequest request) {
        System.out.println(request);
        try {
            String xri = request.getHeader("x-real-ip");
            if (checkIpValid(xri)) return xri;
            String xff = request.getHeader("x-forwarded-for");
            if (checkIpValid(xff)) return toSingleIp(xff);
            String pci = request.getHeader("Proxy-Client-IP");
            if (checkIpValid(pci)) return pci;
            String wl_pci = request.getHeader("WL-Proxy-Client-IP");
            if (checkIpValid(wl_pci)) return wl_pci;
            String ra = request.getRemoteAddr();
            if (LOCALHOST.equals(ra)) {
                try {
                    return InetAddress.getLocalHost().getHostAddress();
                } catch (UnknownHostException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                    return ra;
                }
            }
            return ra;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
