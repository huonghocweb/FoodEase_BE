package poly.foodease.Config;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
// Cấu hình cho broker và cho phép gửi tin nhắn gửi qua Socket
// Cấu hình trong Security để cho phép mở đường dẫn /ws
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // WebSocketMessageBrokerConfigurer cho phép cấu hình liên quan đến webSocket như endpoint , broker tin nhắn
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Khai báo điểm webSocket tại /ws
        // setAllowedOriginPatterns("*") cho phép mọi nguồn có thể kết nối tới điểm này
        // withSockJS cho phép hỗ trợ thư viện nếu font-end không hỗ trợ SockJS
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
        //  registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS();
    }

    // Hàm này cấu hình các đường dẫn bắt đầu để client và server gửi tin nhắn
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Cho phép server gửi tin nhắn đến font-end với đường dẫn topic
        registry.enableSimpleBroker("/topic");
        // Client sẽ gửi tin nhắn đến server bắt đầu đường dẫn bằng /app
        registry.setApplicationDestinationPrefixes("/app");
    }
}
