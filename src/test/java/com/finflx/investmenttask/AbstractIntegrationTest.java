package com.finflx.investmenttask;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finflx.investmenttask.application.service.UserService;
import com.finflx.investmenttask.domain.entity.User;
import com.finflx.investmenttask.domain.enumuration.UserRole;
import com.finflx.investmenttask.infrastructure.util.JwtUtils;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = InvestmentTaskApplication.class)
@AutoConfigureMockMvc
public abstract class AbstractIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper mapper;

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserService userService;

    protected String getInvestorAccessToken() {
        return "Bearer " + TestTokenProvider.getInvestorAuthToken(jwtUtils);
    }

    protected String getCustomAccessToken(User user) {
        return "Bearer " + TestTokenProvider.getCustomAuthToken(jwtUtils, user);
    }

    protected User registerInvestorUser() {
        return userService.create(UUID.randomUUID() + "investor@finflx.com", "password", UserRole.INVESTOR);
    }
}
