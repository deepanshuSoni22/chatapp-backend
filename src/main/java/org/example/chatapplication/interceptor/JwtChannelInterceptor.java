package org.example.chatapplication.interceptor;

import lombok.RequiredArgsConstructor;
import org.example.chatapplication.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private static final Logger logger = LoggerFactory.getLogger(JwtChannelInterceptor.class);

    @Override
    public Message preSend(Message message, MessageChannel channel) {

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(
                message, StompHeaderAccessor.class
        );

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");
            logger.info("[WebSocket] CONNECT received with auth header: {}", authHeader != null ? "present" : "missing");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                try {
                    String jwt = authHeader.substring(7);
                    String username = jwtService.extractUsername(jwt);
                    logger.info("[WebSocket] JWT username extracted: {}", username);

                    UserDetails user = userDetailsService.loadUserByUsername(username);
                    logger.info("[WebSocket] User loaded successfully: {}", username);

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    accessor.setUser(auth);
                    logger.info("[WebSocket] Principal set for user: {}", username);

                } catch (Exception e) {
                    logger.error("[WebSocket] JWT validation failed", e);
                    // Option A: Disconnect the client
                    throw new RuntimeException("JWT validation failed", e);
                    // Option B: Or let it connect without auth (not recommended)
                }
            } else {
                logger.warn("[WebSocket] No authorization header or invalid format");
            }
        }

        return message;
    }
}