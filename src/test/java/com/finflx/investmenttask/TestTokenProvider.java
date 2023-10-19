package com.finflx.investmenttask;

import com.finflx.investmenttask.domain.entity.User;
import com.finflx.investmenttask.domain.enumuration.UserRole;
import com.finflx.investmenttask.infrastructure.util.JwtUtils;

public class TestTokenProvider {
    private static String investorAuthToken;

    public static String getInvestorAuthToken(JwtUtils jwtUtils) {
        if (investorAuthToken == null) {
            investorAuthToken = jwtUtils.generateToken(
                    99L,
                    "investor99@finflx.com",
                    UserRole.INVESTOR.toString());
        }
        return investorAuthToken;
    }

    public static User getInvestorEntity() {
        User user = new User();
        user.setId(1L);
        user.setEmail("a@b.com");
        user.setScopeType(UserRole.INVESTOR);
        user.setPassword("1111111");
        return user;
    }

    public static User getVisitorEntity() {
        User user = new User();
        user.setId(2L);
        user.setEmail("a@bb.com");
        user.setScopeType(UserRole.VISITOR);
        user.setPassword("1111111");
        return user;
    }

    public static String getCustomAuthToken(JwtUtils jwtUtils, User customUser) {
        return jwtUtils.generateToken(
                customUser.getId(),
                customUser.getEmail(),
                customUser.getScopeType().toString()
        );
    }
}
