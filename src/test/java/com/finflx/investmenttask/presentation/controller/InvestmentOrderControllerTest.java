package com.finflx.investmenttask.presentation.controller;

import com.finflx.investmenttask.AbstractIntegrationTest;
import com.finflx.investmenttask.TestTokenProvider;
import com.finflx.investmenttask.application.dto.InvestmentOrderRequest;
import com.finflx.investmenttask.domain.entity.InvestmentAccount;
import com.finflx.investmenttask.domain.entity.User;
import com.finflx.investmenttask.domain.enumuration.OrderStatus;
import com.finflx.investmenttask.domain.enumuration.OrderType;
import com.finflx.investmenttask.domain.repository.InvestmentAccountRepository;
import com.finflx.investmenttask.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static com.finflx.investmenttask.domain.Routes.ORDER_URL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class InvestmentOrderControllerTest extends AbstractIntegrationTest {
    private InvestmentOrderRequest request;
    private User user;
    private static final String ORDER_API_URL = ORDER_URL;

    @Autowired
    private InvestmentAccountRepository investmentAccountRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        investmentAccountRepository.deleteAll();
        userRepository.deleteAll();

        user = TestTokenProvider.getInvestorEntity();
        user = userRepository.save(user);

        InvestmentAccount account = new InvestmentAccount();
        account.setOwner(user);
        account.setBalance(new BigDecimal("500.00"));
        account.setAccountNumber("1234567890");
        account = investmentAccountRepository.save(account);

        request = new InvestmentOrderRequest();
        request.setOrderType(OrderType.BUY);
        request.setAccountId(account.getAccountId());
        request.setOrderAmount(new BigDecimal("20.00"));
    }

    @Test
    void testPlaceOrder() throws Exception {
        mockMvc.perform(post(ORDER_API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", getCustomAccessToken(user))
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderAmount").value(new BigDecimal("20.0")))
                .andExpect(jsonPath("$.orderStatus").value(OrderStatus.PLACED.name()));
    }
}
