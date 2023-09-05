package team.beatcode.consumer.socket;

import lombok.NonNull;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PushHandler extends AbstractWebSocketHandler {

    //******************************************公开配置

    public static final String ATTRIBUTES_TOKEN_STRING = "Token";

    //******************************************内部配置

    /**
     * 兼容并行的哈希表，Key用来唯一标识一个用户，存储现存的用户连接
     * Key存在WebSocketSession中，这里就用用户的Token当身份了
     */
    private static final Map<String, WebSocketSession> users =
            new ConcurrentHashMap<>();

    private String getTokenInSession(@NonNull WebSocketSession session) {
        Object token = session.getAttributes().get(ATTRIBUTES_TOKEN_STRING);
        if (token == null) {
            System.out.printf(
                    "Session doesn't contain attribute %s: \n\t%s\n",
                    ATTRIBUTES_TOKEN_STRING,
                    session);
            return null;
        }
        else return (String) token;
    }

    //******************************************独特业务

    public void sendTextToUser(String token, String content) {
        WebSocketSession session = users.get(token);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(content));
            } catch (IOException e) {
                System.out.printf(
                        "IOE when sending text to user %s\n\t\ncontent: %s",
                        token, content);
            }
        }
        else {
            System.out.printf("Can't find user %s and can't send %s\n",
                    token, content);
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
        String token = getTokenInSession(session);
        if (token != null) {
            users.put(token, session);
            String info = String.format("User %s connected", token);
            sendTextToUser(token, info);
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
        String token = this.getTokenInSession(session);
        if (token != null) {
            users.remove(token);
            System.out.printf("User %s disconnected\n", token);
        } else {
            System.out.printf(
                    "User close session but no token\n\t%s\n",
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
