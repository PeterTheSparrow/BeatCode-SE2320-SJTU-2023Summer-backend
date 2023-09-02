package team.beatcode.consumer.utils.msg;

import sjtu.reins.web.utils.MessageEnumInterface;

public enum MsgEnum implements MessageEnumInterface{
    SUCCESS(0, "成功"),

    FAIL(1, "失败"),

    EMAIL_EXIST_FAULT(4, "邮箱已存在"),

    CODE_ERROR(5, "验证码错误"),

    CODE_EXPIRED(6, "验证码过期"),

    ;

    private final int status;
    private final String msg;
    MsgEnum(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }
}
