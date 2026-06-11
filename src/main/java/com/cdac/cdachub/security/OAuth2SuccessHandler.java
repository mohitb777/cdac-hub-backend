package com.cdac.cdachub.security;

import com.cdac.cdachub.model.User;
import com.cdac.cdachub.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String googleId = oAuth2User.getAttribute("sub");
        String avatar = oAuth2User.getAttribute("picture");

        // Save user if first time login
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = User.builder()
                    .email(email)
                    .name(name)
                    .googleId(googleId)
                    .avatarUrl(avatar)
                    .role(User.Role.STUDENT)
                    .build();
            return userRepository.save(newUser);
        });

        // Generate JWT
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        // Redirect to frontend with token
        response.sendRedirect("http://localhost:5173/auth/callback?token=" + token);
    }
}