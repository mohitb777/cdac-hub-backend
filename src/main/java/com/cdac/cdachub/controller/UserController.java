package com.cdac.cdachub.controller;

import com.cdac.cdachub.model.User;
import com.cdac.cdachub.repository.UserRepository;
import com.cdac.cdachub.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    // Frontend calls this to know who is logged in
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        String email = AuthUtil.getCurrentUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(user);
    }
}