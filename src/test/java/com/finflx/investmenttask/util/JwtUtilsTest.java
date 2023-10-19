package com.finflx.investmenttask.util;

import com.finflx.investmenttask.infrastructure.util.JwtUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JwtUtilsTest {

    private final JwtUtils jwtUtils;

    JwtUtilsTest() {
        this.jwtUtils = new JwtUtils("testSecret", 3600);
    }


    @Test
    void generateToken() {
        String token = jwtUtils.generateToken(1L, "username", "ROLE_USER");
        assertNotNull(token);
    }

    @Test
    void validateTokenValid() {
        String token = generateTestToken();
        assertTrue(jwtUtils.validateToken(token));
    }

    @Test
    void validateTokenInvalid() {
        String token = "invalidToken";
        assertFalse(jwtUtils.validateToken(token));
    }

    @Test
    void extractUsername() {
        String token = generateTestToken();
        assertEquals("username", jwtUtils.extractUsername(token));
    }

    @Test
    void extractScopeType() {
        String token = generateTestToken();
        assertEquals("ROLE_USER", jwtUtils.extractScopeType(token));
    }

    private String generateTestToken() {
        return jwtUtils.generateToken(1L, "username", "ROLE_USER");
    }
}
