package com.finflx.investmenttask.presentation.controller;

import com.finflx.investmenttask.AbstractIntegrationTest;
import com.finflx.investmenttask.TestTokenProvider;
import com.finflx.investmenttask.application.dto.InvestmentAccountRequest;
import com.finflx.investmenttask.domain.entity.User;
import com.finflx.investmenttask.domain.enumuration.UserRole;
import com.finflx.investmenttask.domain.repository.InvestmentAccountRepository;
import com.finflx.investmenttask.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static com.finflx.investmenttask.domain.Routes.ACCOUNT_URL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class InvestmentAccountControllerTest extends AbstractIntegrationTest {
    private User user;
    private InvestmentAccountRequest request;
    private static final String ACCOUNT_API_URL = ACCOUNT_URL;
    private static final String VALID_ACCOUNT_NUMBER = "1234567890";
    private static final BigDecimal VALID_ACCOUNT_BALANCE = new BigDecimal(1000);

    @Autowired
    private InvestmentAccountRepository investmentAccountRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        investmentAccountRepository.deleteAll();
        userRepository.deleteAll();

        user = TestTokenProvider.getInvestorEntity();
        userRepository.save(user);

        request = new InvestmentAccountRequest();
        request.setAccountNumber(VALID_ACCOUNT_NUMBER);
        request.setBalance(VALID_ACCOUNT_BALANCE);
    }

    @Test
    void testCreateAccount() throws Exception {
        mockMvc.perform(post(ACCOUNT_API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", getCustomAccessToken(user))
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountNumber").value(VALID_ACCOUNT_NUMBER))
                .andExpect(jsonPath("$.balance").value(VALID_ACCOUNT_BALANCE));
    }
}