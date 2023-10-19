package com.finflx.investmenttask.presentation.controller;

import com.finflx.investmenttask.AbstractIntegrationTest;
import com.finflx.investmenttask.application.dto.LoginRequest;
import com.finflx.investmenttask.application.dto.RefreshTokenRequest;
import com.finflx.investmenttask.application.dto.RefreshTokenResponse;
import com.finflx.investmenttask.application.service.RefreshTokenService;
import com.finflx.investmenttask.application.service.UserService;
import com.finflx.investmenttask.domain.entity.User;
import com.finflx.investmenttask.domain.enumuration.UserRole;
import com.finflx.investmenttask.domain.repository.UserRepository;
import com.finflx.investmenttask.infrastructure.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.finflx.investmenttask.domain.Routes.AUTH_URL;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthControllerTest extends AbstractIntegrationTest{
    private static final String AUTH_API_URL = AUTH_URL;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testLogin() throws Exception {

        userService.create("user@test.com", "password", UserRole.INVESTOR);

        LoginRequest request = new LoginRequest();
        request.setEmail("user@test.com");
        request.setPassword("password");

        mockMvc.perform(MockMvcRequestBuilders.post(AUTH_API_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.email").value("user@test.com"))
                .andExpect(jsonPath("$.scopeType").isNotEmpty())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty());
    }

    @Test
    void testRefreshToken() throws Exception {

        User user = userService.create("user@test.com", "password", UserRole.INVESTOR);
        String refreshToken = refreshTokenService.generateForUser(user).getToken();

        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken(refreshToken);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(AUTH_API_URL + "/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").value(refreshToken))
                .andReturn();

        assertNotNull(refreshToken);

        RefreshTokenResponse responseContent = mapper.readValue(
                result.getResponse().getContentAsString(),
                RefreshTokenResponse.class);

        assertEquals("user@test.com", jwtUtils.extractUsername(responseContent.getAccessToken()));
        assertEquals("INVESTOR", jwtUtils.extractScopeType(responseContent.getAccessToken()));
    }
}