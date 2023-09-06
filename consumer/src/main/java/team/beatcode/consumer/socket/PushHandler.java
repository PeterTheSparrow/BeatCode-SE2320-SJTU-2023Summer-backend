package team.beatcode.consumer.socket;

import lombok.NonNull;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PushHandler extends AbstractWebSocketHandler {

    //******************************************公开配置

    public static final String ATTRIBUTES_USERID_INTEGER = "UserId";

    //******************************************内部配置

    /**
     * 兼容并行的哈希表，Key用来唯一标识一个用户，存储现存的用户连接
     * Key存在WebSocketSession中，使用用户的Id当作Key
     */
    private static final Map<Integer, WebSocketSession> users =
            new ConcurrentHashMap<>();

    private Integer getUserIdInSession(@NonNull WebSocketSession session) {
        Object uid = session.getAttributes().get(ATTRIBUTES_USERID_INTEGER);
        if (uid == null) {
            System.out.printf(
                    "Session doesn't contain attribute %s: \n\t%s\n",
                    ATTRIBUTES_USERID_INTEGER,
                    session);
            return null;
        }
        else return (Integer) uid;
    }

    //******************************************独特业务

    public void sendTextToUser(Integer uid, String content) {
        WebSocketSession session = users.get(uid);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(content));
            } catch (IOException e) {
                System.out.printf(
                        "IOE when sending text to user %s\n\t\ncontent: %s",
                        uid, content);
            }
        }
        else {
            System.out.printf("Can't find user %s and can't send %s\n",
                    uid, content);
        }
    }

    //******************************************继承内容

    /**
     * 新链接建立时调用该函数
     */
    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        System.out.println("get connected");
        System.out.printf("Attributes: %s\n", session.getAttributes());
        Integer uid = getUserIdInSession(session);
        if (uid != null) {
            users.put(uid, session);
            String info = String.format("User %s connected", uid);
            sendTextToUser(uid, info);
            System.out.println(info);
        }
    }

    /**
     * 链接关闭回调
     */
    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session,
                                      @NonNull CloseStatus status) {
        System.out.println("dis connected");
        Integer uid = this.getUserIdInSession(session);
        if (uid != null) {
            users.remove(uid);
            System.out.printf("User %s disconnected\n", uid);
        } else {
            System.out.printf(
                    "User close session but no UserId\n\t%s\n",
                    session);
        }
    }

    /**
     * 收到文本格式数据回调
     */
    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session,
                                     @NonNull TextMessage message) {
        System.out.printf("Got text msg: %s\n", message.getPayload());
    }

    /**
     * 收到二进制类型数据回调
     */
    @Override
    protected void handleBinaryMessage(@NonNull WebSocketSession session,
                                       @NonNull BinaryMessage message) {
        System.out.printf("Got bin msg: %d\n", message.getPayloadLength());
    }

    /**
     * 心跳检测回调
     */
    @Override
    protected void handlePongMessage(@NonNull WebSocketSession session,
                                     @NonNull PongMessage message) throws Exception {
        super.handlePongMessage(session, message);
    }

    /**
     * 错误处理
     */
    @Override
    public void handleTransportError(@NonNull WebSocketSession session,
                                     @NonNull Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
    }
}
