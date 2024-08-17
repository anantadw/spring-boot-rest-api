package com.anantadw.spring_boot_api.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.anantadw.spring_boot_api.service.JwtService;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userUsername;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);

            return;
        }

        try {
            log.info("AuthHeader: {}", authHeader);
            jwt = authHeader.substring(7);
            log.info(jwt);
            userUsername = jwtService.extractUsername(jwt);
            log.info(userUsername);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            log.info("Authentication: {}", authentication);

            if (userUsername != null && authentication == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userUsername);
                log.info("UserDetails: {}", userDetails);
                log.info("UserDetailsUsername: {}", userDetails.getUsername());

                log.info("Token Valid: {}", jwtService.isTokenValid(jwt, userDetails));
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());
                    log.info("AuthToken: {}", authToken);

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Error logging in: {}", e.getMessage());
            // e.printStackTrace();
        }
    }
}
