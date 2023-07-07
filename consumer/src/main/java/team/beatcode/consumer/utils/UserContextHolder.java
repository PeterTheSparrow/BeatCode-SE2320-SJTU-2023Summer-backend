package team.beatcode.consumer.utils;

public class UserContextHolder {
    private static final ThreadLocal<Integer> userAccountHolder
            = new ThreadLocal<>();

    public static void setUserAccount(Integer userAccount) {
        userAccountHolder.set(userAccount);
    }

    /**
     * @return 你可以在Controller里调用这个方法获取当前登录的用户id
     */
    public static Integer getUserAccount() {
        return userAccountHolder.get();
    }

    public static void clear() {
        userAccountHolder.remove();
    }
}
