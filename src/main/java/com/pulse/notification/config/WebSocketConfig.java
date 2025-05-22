package com.pulse.notification.config;

import com.pulse.notification.service.UserIdHandshakeHandler;
import com.pulse.security.service.JwtService;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtService jwtService;
    public WebSocketConfig(JwtService jwtService) { this.jwtService = jwtService; }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry reg) {
        reg.addEndpoint("/ws")
                .addInterceptors(new JwtHandshakeInterceptor(jwtService))
                .setHandshakeHandler(new UserIdHandshakeHandler())
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry cfg) {
        cfg.enableSimpleBroker("/queue");               // user queues
        cfg.setUserDestinationPrefix("/user");         // /user/queue/…
    }
}
