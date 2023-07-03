package team.beatcode.auth.utils;

import lombok.NonNull;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IpInStr {
    public static byte[] ipAddrStrToHex(@NonNull String addr) {
        String ipInString = addr.replace(" ", "");
        if (ipInString.contains(":"))
            return ipv6ToBytes(ipInString);
        else
            return ipv4ToBytes(ipInString);
    }

    public static String ipAddrHexToStr(byte[] addr) {
        try {
            String ip = InetAddress.getByAddress(addr).toString();
            return ip.substring(ip.indexOf('/') + 1).trim();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * Ip Translation Tools
     * Reference: https://www.cnblogs.com/seaspring/p/5679055.html
     */

    /**
     * ipv6地址转有符号byte[17]
     * @param   ipv6 字符串形式的IP地址
     * @return big integer number
     */
    private static byte[] ipv6ToBytes(String ipv6) {
        byte[] ret = new byte[16];
        ret[0] = 0;
        int ib = 15;
        boolean comFlag = false;// ipv4混合模式标记
        if (ipv6.startsWith(":"))// 去掉开头的冒号
            ipv6 = ipv6.substring(1);
        String[] groups = ipv6.split(":");
        for (int ig = groups.length - 1; ig > -1; ig--) {// 反向扫描
            if (groups[ig].contains(".")) {
                // 出现ipv4混合模式
                byte[] temp = ipv4ToBytes(groups[ig]);
                ret[ib--] = temp[4];
                ret[ib--] = temp[3];
                ret[ib--] = temp[2];
                ret[ib--] = temp[1];
                comFlag = true;
            } else if ("".equals(groups[ig])) {
                // 出现零长度压缩,计算缺少的组数
                int zlg = 9 - (groups.length + (comFlag ? 1 : 0));
                while (zlg-- > 0) {// 将这些组置0
                    ret[ib--] = 0;
                    ret[ib--] = 0;
                }
            } else {
                int temp = Integer.parseInt(groups[ig], 16);
                ret[ib--] = (byte) temp;
                ret[ib--] = (byte) (temp >> 8);
            }
        }
        return ret;
    }

    /**
     * ipv4地址转有符号byte[4]
     * @param ipv4 字符串的IPV4地址
     * @return big integer number
     */
    private static byte[] ipv4ToBytes(String ipv4) {
        byte[] ret = new byte[4];
        ret[0] = 0;
        // 先找到IP地址字符串中.的位置
        int position1 = ipv4.indexOf(".");
        int position2 = ipv4.indexOf(".", position1 + 1);
        int position3 = ipv4.indexOf(".", position2 + 1);
        // 将每个.之间的字符串转换成整型
        ret[0] = (byte) Integer.parseInt(ipv4.substring(0, position1));
        ret[1] = (byte) Integer.parseInt(ipv4.substring(position1 + 1, position2));
        ret[2] = (byte) Integer.parseInt(ipv4.substring(position2 + 1, position3));
        ret[3] = (byte) Integer.parseInt(ipv4.substring(position3 + 1));
        return ret;
    }
}
