package com.example.DoAnJ2EE.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        System.out.println("REQUEST URI = " + request.getRequestURI());
        System.out.println("AUTH HEADER = " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("NO TOKEN");
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);
        boolean valid = jwtService.isTokenValid(jwt);
        System.out.println("JWT VALID = " + valid);

        if (!valid) {
            filterChain.doFilter(request, response);
            return;
        }

        String subject = jwtService.extractSubject(jwt);
        System.out.println("JWT SUBJECT = " + subject);

        if (subject != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(subject);
            System.out.println("USER LOADED = " + userDetails.getUsername());
            System.out.println("AUTHORITIES = " + userDetails.getAuthorities());

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            authenticationToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            System.out.println("AUTH SET OK");
        }

        filterChain.doFilter(request, response);
    }
}