package team.beatcode.consumer.utils;

import jakarta.servlet.http.HttpServletResponse;

public class Macros {
    public static final String TOKEN_NAME = "golden-class-token";

    public static final String USER_CONTEXT_ID = "user_id";
    public static final String USER_CONTEXT_NAME = "user_name";
    public static final String USER_CONTEXT_ROLE = "user_role";

    public static void implResponse(HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");

        // @Explorer Why you need these?
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }
}
