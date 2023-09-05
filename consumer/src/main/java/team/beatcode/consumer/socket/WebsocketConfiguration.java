package team.beatcode.consumer.socket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebsocketConfiguration implements WebSocketConfigurer {
    @Bean
    PushHandler pushHandlerBean() {
        return new PushHandler();
    }

    @Bean
    WebSocketInterceptor webSocketInterceptorBean() {
        return new WebSocketInterceptor();
    }

    /**
     * 将Handler注册并映射到路径<br>
     * 连接到地址：ws://【服务的地址】/info?Token=【用户的登录身份token】
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        //withSockJS()兼容SockJS
        registry.addHandler(pushHandlerBean(), "/info")
                .addInterceptors(webSocketInterceptorBean())
                .setAllowedOriginPatterns("*");
        registry.addHandler(pushHandlerBean(), "/socketJs/info")
                .addInterceptors(webSocketInterceptorBean())
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
    //教程来自 https://blog.csdn.net/wang1369125334/article/details/53418184
}
