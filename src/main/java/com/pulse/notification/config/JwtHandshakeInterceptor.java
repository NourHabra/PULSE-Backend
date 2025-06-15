package com.pulse.notification.config;


import com.pulse.security.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtService jwtService;

    public JwtHandshakeInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {

        if (request instanceof ServletServerHttpRequest servlet) {
            HttpServletRequest raw = servlet.getServletRequest();
            String auth = raw.getHeader("Authorization");
            if (auth != null && auth.startsWith("Bearer ")) {
                String token = auth.substring(7);

                var userDetails = jwtService.getUserFromToken(token);
                if (jwtService.isTokenValid(token, userDetails)) {
                    Long userId = ((com.pulse.user.model.User) userDetails).getUserId();
                    String role = userDetails.getAuthorities()
                            .iterator().next()
                            .getAuthority();

                    attributes.put("userId", userId);
                    attributes.put("role", role);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) { }
}
