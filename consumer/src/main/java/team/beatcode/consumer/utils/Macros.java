package team.beatcode.consumer.utils;

import jakarta.servlet.http.HttpServletResponse;

public class Macros {
    public static final String AUTH_CHECK_SUCCESS = "yes";
    public static final String AUTH_CHECK_FAIL    = "no";
    public static final String AUTH_CHECK_ERROR   = "wtf";


    public static final String X_REAL_IP = "x-real-ip";
    public static final String X_FORWARDED_FOR = "x-forwarded-for";
    public static final String PROXY_CLIENT_IP = "proxy-client-ip";
    public static final String WL_PROXY_CLIENT_IP = "wl-proxy-client-ip";

    public static void implResponse(HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");

        // @Explorer Why you need these?
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }
}
