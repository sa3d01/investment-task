package com.finflx.investmenttask.infrastructure.util;

import com.finflx.investmenttask.infrastructure.security.UserDetailsDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    public static final String INVESTOR_ROLE = "INVESTOR";

    private SecurityUtils() {
    }

    public static Long getAuthUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetailsDto userDetails = (UserDetailsDto) authentication.getPrincipal();
            return userDetails.getId();
        }
        return null;
    }
}
