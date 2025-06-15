package com.pulse.notification.service;

import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import com.pulse.notification.config.StompPrincipal;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;


@Component
public class AutoSubscribeInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> postReceive(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor acc = StompHeaderAccessor.wrap(message);
        if (StompCommand.CONNECT.equals(acc.getCommand())) {
            StompPrincipal p = (StompPrincipal) acc.getUser();
            String dest = p.getRole().equals("ROLE_PATIENT")
                    ? "/topic/patient." + p.getUserId()
                    : "/topic/doctor."  + p.getUserId();

            SubscriptionRegistry.add(acc.getSessionId(), dest);
        }
        return message;
    }
}
