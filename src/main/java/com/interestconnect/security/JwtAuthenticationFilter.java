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

        

        String authHeader = request.getHeader("Authorization");
       

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("❌ No Bearer token found.");
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);
        

        boolean valid = jwtService.isTokenValid(jwt);
        

        if (!valid) {
            System.out.println("❌ Invalid JWT.");
            filterChain.doFilter(request, response);
            return;
        }

        String email = jwtService.extractEmail(jwt);
      

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            System.out.println("Loading user from database...");

            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(email);

            

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

       

        filterChain.doFilter(request, response);
    }
}
