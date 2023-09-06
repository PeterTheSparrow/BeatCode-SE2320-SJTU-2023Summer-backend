package team.beatcode.user.utils;

import sjtu.reins.web.utils.MessageEnumInterface;

public enum MsgEnum implements MessageEnumInterface{
    SUCCESS(0, "成功"),

    FAIL(1, "失败"),

    USER_NOT_EXIST(2, "用户不存在"),

    USER_NAME_EXIST_FAULT(3, "用户名已存在"),

    EMAIL_EXIST_FAULT(4, "邮箱已存在"),

    CODE_ERROR(5, "验证码错误"),

    CODE_EXPIRED(6, "验证码过期"),

    PARAM_FAIL(7, "缺少参数"),

    PAGE_NEGATIVE(7, "页码不能为负数"),

    PAGE_MALICE(8, "页码过小"),

    PROBLEM_NOT_EXIST(9, "题目不存在"),
    ;

    private final int status;
    private final String msg;
    MsgEnum(int status, String msg) {
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
