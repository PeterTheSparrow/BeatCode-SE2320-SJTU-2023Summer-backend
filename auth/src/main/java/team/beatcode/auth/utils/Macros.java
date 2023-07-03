package team.beatcode.auth.utils;

/**
 * 常量与设置
 */
public class Macros {
    public static final String AUTH_CHECK_SUCCESS = "yes";
    public static final String AUTH_CHECK_FAIL    = "no";
    public static final String AUTH_CHECK_ERROR   = "wtf";

    public static final int AUTH_CODE_ADMIN = 1;
    public static final int AUTH_CODE_USER  = 0;

    public static final long AUTH_LIFE = 14400000; // 4 hours
    public static final long AUTH_MAX_REFRESH = 864000000; // 10 days
}
