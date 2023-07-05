package sjtu.reins.web.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

public class Message {
    @Getter @Setter
    private int status;
    @Getter @Setter
    private String msg;
    @Getter @Setter
    private Object data;

    public Message(MessageEnumInterface msg, Object data){
        this.status = msg.getStatus();
        this.msg = msg.getMsg();
        this.data = data;
    }
    public Message(MessageEnumInterface msg){
        this.status = msg.getStatus();
        this.msg = msg.getMsg();
        this.data = "";
    }

    private static final ObjectMapper mapper = new ObjectMapper();
    @Override
    public String toString() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
