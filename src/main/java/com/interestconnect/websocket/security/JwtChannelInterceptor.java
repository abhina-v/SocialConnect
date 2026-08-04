package com.interestconnect.websocket.security;

import com.interestconnect.security.CustomUserDetailsService;
import com.interestconnect.security.JwtService;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtChannelInterceptor(
            JwtService jwtService,
            CustomUserDetailsService userDetailsService) {

        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Message<?> preSend(
            Message<?> message,
            MessageChannel channel) {

        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(
                        message,
                        StompHeaderAccessor.class);

        if (accessor != null &&
                StompCommand.CONNECT.equals(accessor.getCommand())) {

            String authHeader =
                    accessor.getFirstNativeHeader("Authorization");

            if (authHeader != null &&
                    authHeader.startsWith("Bearer ")) {

                String jwt = authHeader.substring(7);

                if (jwtService.isTokenValid(jwt)) {

                    String email =
                            jwtService.extractEmail(jwt);

                    UserDetails userDetails =
                            userDetailsService.loadUserByUsername(email);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities());

                    accessor.setUser(authentication);
                }
            }
        }

        return message;
    }
}