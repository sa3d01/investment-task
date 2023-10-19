package com.finflx.investmenttask.application.service;

import com.finflx.investmenttask.TestTokenProvider;
import com.finflx.investmenttask.application.dto.InvestmentAccountRequest;
import com.finflx.investmenttask.application.dto.InvestmentAccountResponse;
import com.finflx.investmenttask.domain.entity.InvestmentAccount;
import com.finflx.investmenttask.domain.entity.User;
import com.finflx.investmenttask.domain.repository.InvestmentAccountRepository;
import com.finflx.investmenttask.domain.repository.UserRepository;
import com.finflx.investmenttask.infrastructure.util.SecurityUtils;
import com.finflx.investmenttask.presentation.exception.EntityNotFoundException;
import com.finflx.investmenttask.presentation.exception.NotEligibleToUpdateEntityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class InvestmentAccountServiceTest {
    private InvestmentAccountRequest request;

    @Mock
    private InvestmentAccountRepository investmentAccountRepository;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private InvestmentAccountService investmentAccountService;

    @BeforeEach
    void setUp() {
        investmentAccountRepository.deleteAll();
        userRepository.deleteAll();

        request = new InvestmentAccountRequest();
        request.setAccountNumber("1234567890");
        request.setBalance(new BigDecimal(1000));
    }

    @Test
    void testCreateAccountWithNonExistingUser() {
        // Arrange

        Long mockUserId = 100L;

        try (MockedStatic<SecurityUtils> mocked = Mockito.mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getAuthUserId).thenReturn(mockUserId);

            doThrow(new EntityNotFoundException(User.class.getSimpleName(), "id", mockUserId))
                    .when(userService)
                    .validateUserExistingAndHasInvestorRole(mockUserId);

            // Act & Assert
            assertThrows(EntityNotFoundException.class, () -> investmentAccountService.createAccount(request));
        }
    }

    @Test
    void testCreateAccountWithNonInvestorUser() {
        // Arrange
        User testUser = TestTokenProvider.getVisitorEntity();
        userRepository.save(testUser);

        Long mockUserId = testUser.getId();

        try (MockedStatic<SecurityUtils> mocked = Mockito.mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getAuthUserId).thenReturn(mockUserId);

            doThrow(new NotEligibleToUpdateEntityException(User.class.getSimpleName(), mockUserId))
                    .when(userService)
                    .validateUserExistingAndHasInvestorRole(mockUserId);

            // Act & Assert
            assertThrows(NotEligibleToUpdateEntityException.class, () -> investmentAccountService.createAccount(request));
        }
    }

    @Test
    void testValidateAccountBelongsToUserWithNonExistingAccount() {
        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> investmentAccountService.validateAccountBelongsToUser(1L, 1L));
    }

    @Test
    void testValidateAccountBelongsToUserWithAccountNotBelongingToUser() {
        // Arrange
        User testUser = TestTokenProvider.getInvestorEntity();
        InvestmentAccount testAccount = new InvestmentAccount();
        testAccount.setOwner(testUser);

        when(investmentAccountRepository.findByAccountId(anyLong())).thenReturn(Optional.of(testAccount));

        // Act & Assert
        assertThrows(NotEligibleToUpdateEntityException.class, () -> investmentAccountService.validateAccountBelongsToUser(1L, 2L));
    }

    @Test
    void testCreateAccountWithValidRole() {
        // Arrange
        User testUser = TestTokenProvider.getInvestorEntity();
        userRepository.save(testUser);

        InvestmentAccountResponse expectedResponse = new InvestmentAccountResponse();

        InvestmentAccount testAccount = new InvestmentAccount();

        when(investmentAccountRepository.save(any(InvestmentAccount.class))).thenReturn(testAccount);

        // Act
        InvestmentAccountResponse actual = investmentAccountService.createAccount(request);

        // Assert
        assertEquals(expectedResponse.getAccountId(), actual.getAccountId());
        assertEquals(expectedResponse.getAccountNumber(), actual.getAccountNumber());
    }

    @Test
    void getById() {
        // Arrange
        Long accountId = 1L;
        InvestmentAccount testAccount = new InvestmentAccount();
        testAccount.setAccountId(accountId);
        when(investmentAccountRepository.findByAccountId(accountId)).thenReturn(Optional.of(testAccount));
        // Act
        InvestmentAccount actual = investmentAccountService.getById(accountId);
        // Assert
        assertEquals(testAccount, actual);
    }
}
