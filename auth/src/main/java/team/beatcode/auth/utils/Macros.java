package team.beatcode.auth.utils;

/**
 * 常量与设置
 */
public class Macros {

    public static final int AUTH_CODE_ADMIN = 1;
    public static final int AUTH_CODE_USER  = 0;

    public static final long AUTH_LIFE = 14400000; // 4 hours
    public static final long AUTH_MAX_REFRESH = 864000000; // 10 days

    public static final String TOKEN_NAME = "golden-class-token";
    public static final String IS_ADMIN = "is_admin";

    public static final String USER_CONTEXT_ID = "user_id";
    public static final String USER_CONTEXT_NAME = "user_name";
    public static final String USER_CONTEXT_ROLE = "user_role";
}
