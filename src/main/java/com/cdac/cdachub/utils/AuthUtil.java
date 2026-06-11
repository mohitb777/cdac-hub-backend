package com.cdac.cdachub.utils;

import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtil {

    // Call this anywhere to get the logged in user's email
    public static String getCurrentUserEmail() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal()
                .toString();
    }
}