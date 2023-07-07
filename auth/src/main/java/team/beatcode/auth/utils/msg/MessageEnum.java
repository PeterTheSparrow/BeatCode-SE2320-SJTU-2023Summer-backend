package team.beatcode.auth.utils.msg;

import sjtu.reins.web.utils.MessageEnumInterface;

public enum MessageEnum implements MessageEnumInterface {
    SUCCESS(0, "成功"),

    FAIL(1, "失败"),
    FAULT(2, "出错"),

    USER_EXIST_FAULT(3, "用户名已存在"),

    PARAM_FAIL(100, "缺少参数"),
    IP_FAULT(101, "无法解析访问者ip"),
    USER_NOT_FOUND_FAULT(1000, "找不到指定的用户"),
    USER_BAD_PASS_FAIL(1001, "密码错误"),

    AUTH_SUCCESS(200, "yes"),
    AUTH_FAIL(201, "no"),
    AUTH_ERROR(202, "wtf")
    ;

    private final int status;
    private final String msg;
    MessageEnum(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }
    public String getMsg() {
        return msg;
    }
}
