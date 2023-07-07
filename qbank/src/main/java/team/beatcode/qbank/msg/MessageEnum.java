package team.beatcode.qbank.msg;

import sjtu.reins.web.utils.MessageEnumInterface;

public enum MessageEnum implements MessageEnumInterface {
    SUCCESS(0, "成功"),
    FAIL(1, "失败"),
    FAULT(2, "出错"),

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
