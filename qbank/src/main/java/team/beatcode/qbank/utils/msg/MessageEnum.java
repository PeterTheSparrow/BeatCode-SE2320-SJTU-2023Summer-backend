package team.beatcode.qbank.utils.msg;

import sjtu.reins.web.utils.MessageEnumInterface;

public enum MessageEnum implements MessageEnumInterface {
    SUCCESS(0, "成功"),

    FAIL(1, "失败"),
    FAULT(2, "出错"),

    PARAM_FAIL(100, "缺少参数"),
    SEARCH_DIFFICULTY_UNKNOWN(1020, "不认识的难度"),
    SEARCH_PAGE_FAULT(1021, "找不到Page"),
    SEARCH_PAGE_NEGATIVE(1022, "页数序号从1开始，问就是学Lua学的"),
    SEARCH_PAGE_MALICE(1023, "你是生活多不如意才会把页尺寸设那么小？"),

    ADMIN_PROBLEM_NOT_FOUND(2001, "未找到题目，请先上传题干信息"),
    ADMIN_PROBLEM_PACKAGE_ERROR(2002, "压缩包不符合格式"),
    ADMIN_PROBLEM_LOCKED(2003, "另一位管理员正在修改"),
    ADMIN_PROBLEM_LACK_PARAM(2004, "缺少参数"),

    SYSTEM_ERROR(3000, "后台错误，参见日志"),
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
