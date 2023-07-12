package team.beatcode.qbank.utils.msg;

import lombok.Getter;

public class MessageException extends RuntimeException{
    @Getter
    private final MessageEnum e;

    public MessageException(MessageEnum e) {
        super();
        this.e = e;
    }
}
