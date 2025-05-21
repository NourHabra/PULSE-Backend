package com.pulse.notification.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override          // public → browser or Flutter connects here
    public void registerStompEndpoints(StompEndpointRegistry reg) {
        reg.addEndpoint("/ws")                // ws://HOST/ws
                .setAllowedOriginPatterns("*")     // CORS; tighten in prod
                .withSockJS();                     // optional SockJS fallback
    }

    @Override          // broker topics that clients can SUBSCRIBE to
    public void configureMessageBroker(MessageBrokerRegistry cfg) {
        cfg.enableSimpleBroker("/topic");     // in-memory broker /topic/…
        cfg.setApplicationDestinationPrefixes("/app"); // not used here
    }
}
