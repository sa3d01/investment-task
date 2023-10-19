package com.finflx.investmenttask.application.service;

import com.finflx.investmenttask.TestTokenProvider;
import com.finflx.investmenttask.application.dto.InvestmentOrderRequest;
import com.finflx.investmenttask.application.dto.InvestmentOrderResponse;
import com.finflx.investmenttask.domain.entity.InvestmentAccount;
import com.finflx.investmenttask.domain.entity.InvestmentOrder;
import com.finflx.investmenttask.domain.entity.User;
import com.finflx.investmenttask.domain.enumuration.OrderType;
import com.finflx.investmenttask.domain.repository.InvestmentAccountRepository;
import com.finflx.investmenttask.domain.repository.InvestmentOrderRepository;
import com.finflx.investmenttask.domain.repository.UserRepository;
import com.finflx.investmenttask.infrastructure.util.SecurityUtils;
import com.finflx.investmenttask.presentation.exception.EntityNotFoundException;
import com.finflx.investmenttask.presentation.exception.InsufficientBalanceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvestmentOrderServiceTest {
    @Mock
    private InvestmentOrderRepository investmentOrderRepository;

    @Mock
    private InvestmentAccountRepository investmentAccountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private InvestmentAccountService investmentAccountService;

    @InjectMocks
    private InvestmentOrderService investmentOrderService;

    private InvestmentOrderRequest request;
    private InvestmentAccount account;
    private final User user = TestTokenProvider.getInvestorEntity();

    @BeforeEach
    public void setup() {
        // Set up the test data
        userRepository.save(user);

        account = new InvestmentAccount();
        account.setAccountId(1L);
        account.setBalance(new BigDecimal("200.00"));
        account.setOwner(user);
        investmentAccountRepository.save(account);

        request = new InvestmentOrderRequest();
        request.setAccountId(1L);
        request.setOrderAmount(new BigDecimal("200.00"));
        request.setOrderType(OrderType.BUY);
    }

    @Test
    public void testPlaceOrderWithValidRequest() {
        // Arrange
        when(investmentAccountService.getById(account.getAccountId())).thenReturn(account);
        when(investmentOrderRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        try (MockedStatic<SecurityUtils> mocked = Mockito.mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getAuthUserId).thenReturn(user.getId());

            ArgumentCaptor<InvestmentOrder> captor = ArgumentCaptor.forClass(InvestmentOrder.class);

            // Act
            InvestmentOrderResponse response = investmentOrderService.placeOrder(request);

            // Assert
            assertNotNull(response);
            verify(userService, times(1)).validateUserExistingAndHasInvestorRole(user.getId());
            verify(investmentAccountService, times(1)).validateAccountBelongsToUser(account.getAccountId(), user.getId());
            verify(investmentOrderRepository, times(1)).save(captor.capture());
            InvestmentOrder savedOrder = captor.getValue();

            // Check that the order was created with the correct account and order amount
            assertEquals(request.getAccountId(), savedOrder.getAccount().getAccountId());
            assertEquals(request.getOrderAmount(), savedOrder.getOrderAmount());

            // Check that the account balance was updated correctly
            assertEquals(new BigDecimal("0.00"), account.getBalance());
        }
    }

    @Test
    public void testPlaceOrderInsufficientBalance() {
        // Arrange
        account.setBalance(new BigDecimal("50.00")); // set balance lower than order amount
        when(investmentAccountService.getById(anyLong())).thenReturn(account);

        // Act and Assert
        assertThrows(InsufficientBalanceException.class, () -> investmentOrderService.placeOrder(request));
    }

    @Test
    public void testPlaceOrderEntityNotFoundException() {
        // Arrange
        when(investmentAccountService.getById(anyLong()))
                .thenThrow(new EntityNotFoundException(InvestmentAccount.class.getSimpleName(), "id", 1L));

        // Act and Assert
        assertThrows(EntityNotFoundException.class, () -> investmentOrderService.placeOrder(request));
    }
}
