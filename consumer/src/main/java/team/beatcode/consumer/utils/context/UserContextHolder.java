package team.beatcode.consumer.utils.context;

public class UserContextHolder {
    private static final ThreadLocal<UserContext> userAccountHolder
            = new ThreadLocal<>();

    public static void setUserAccount(UserContext userAccount) {
        userAccountHolder.set(userAccount);
    }

    /**
     * @return 你可以在Controller里调用这个方法获取当前登录的用户id
     */
    public static UserContext getUserAccount() {
        return userAccountHolder.get();
    }

    public static void clear() {
        userAccountHolder.remove();
    }

    private static final ThreadLocal<String> tokenHolder = new ThreadLocal<>();
    public static void setToken(String token) {
        tokenHolder.set(token);
    }
    public static String getToken() {
        return tokenHolder.get();
    }
    public static void clearToken() {
        tokenHolder.remove();
    }

}
