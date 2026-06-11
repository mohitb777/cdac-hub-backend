package com.cdac.cdachub.security;

import com.cdac.cdachub.model.User;
import com.cdac.cdachub.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // Step 1: Read the Authorization header
        // It looks like: "Bearer eyJhbGciOiJIUzI1NiJ9..."
        String authHeader = request.getHeader("Authorization");

        // Step 2: Check if header exists and starts with Bearer
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // No token found, continue without setting auth
            filterChain.doFilter(request, response);
            return;
        }

        // Step 3: Extract just the token part (remove "Bearer ")
        String token = authHeader.substring(7);

        // Step 4: Validate token
        if (!jwtUtil.isTokenValid(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Step 5: Get email from token
        String email = jwtUtil.extractEmail(token);

        // Step 6: Load user from database
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // Step 7: Tell Spring Security this user is authenticated
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(
                email,  // principal (who they are)
                null,   // credentials (not needed)
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
            );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Step 8: Continue to the controller
        filterChain.doFilter(request, response);
    }
}