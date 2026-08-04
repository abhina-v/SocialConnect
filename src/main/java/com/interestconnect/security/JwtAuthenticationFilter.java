package com.interestconnect.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService,
                                   CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("\n========== JWT FILTER ==========");
        System.out.println("Request URI: " + request.getRequestURI());

        String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization Header: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("❌ No Bearer token found.");
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);
        System.out.println("JWT Token: " + jwt);

        boolean valid = jwtService.isTokenValid(jwt);
        System.out.println("Token Valid: " + valid);

        if (!valid) {
            System.out.println("❌ Invalid JWT.");
            filterChain.doFilter(request, response);
            return;
        }

        String email = jwtService.extractEmail(jwt);
        System.out.println("Extracted Email: " + email);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            System.out.println("Loading user from database...");

            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(email);

            System.out.println("User Loaded: " + userDetails.getUsername());

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());

            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            System.out.println("✅ Authentication successfully set in SecurityContext.");
        } else {
            System.out.println("Authentication already exists or email is null.");
        }

        System.out.println("========== END JWT FILTER ==========\n");

        filterChain.doFilter(request, response);
    }
}