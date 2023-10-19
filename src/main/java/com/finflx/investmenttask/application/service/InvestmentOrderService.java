package com.finflx.investmenttask.application.service;

import com.finflx.investmenttask.domain.entity.InvestmentAccount;
import com.finflx.investmenttask.domain.enumuration.OrderType;
import com.finflx.investmenttask.infrastructure.util.SecurityUtils;
import com.finflx.investmenttask.presentation.exception.InsufficientBalanceException;
import com.finflx.investmenttask.application.dto.InvestmentOrderRequest;
import com.finflx.investmenttask.application.dto.InvestmentOrderResponse;
import com.finflx.investmenttask.domain.entity.InvestmentOrder;
import com.finflx.investmenttask.application.mapper.InvestmentOrderMapper;
import com.finflx.investmenttask.domain.repository.InvestmentOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class InvestmentOrderService {
    private final InvestmentOrderRepository investmentOrderRepository;
    private final UserService userService;
    private final InvestmentAccountService investmentAccountService;

    public InvestmentOrderService(InvestmentOrderRepository investmentOrderRepository,
                                  UserService userService,
                                  InvestmentAccountService investmentAccountService) {
        this.investmentOrderRepository = investmentOrderRepository;
        this.userService = userService;
        this.investmentAccountService = investmentAccountService;
    }

    @Transactional
    public InvestmentOrderResponse placeOrder(InvestmentOrderRequest request) {
        Long authUserId = SecurityUtils.getAuthUserId();

        userService.validateUserExistingAndHasInvestorRole(authUserId);
        investmentAccountService.validateAccountBelongsToUser(request.getAccountId(), authUserId);

        InvestmentAccount investmentAccount = investmentAccountService.getById(request.getAccountId());
        validateOrderBalance(request.getOrderAmount(), investmentAccount);

        InvestmentOrder order = InvestmentOrderMapper.INSTANCE.map(request);
        updateAccountBalance(order.getOrderType(),investmentAccount,request.getOrderAmount());

        order.setAccount(investmentAccount);

        return InvestmentOrderMapper.INSTANCE.map(investmentOrderRepository.save(order));
    }

    void validateOrderBalance(BigDecimal amount, InvestmentAccount investmentAccount) {
        if (investmentAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("The account balance is insufficient to place this order.");
        }
    }

    void updateAccountBalance(OrderType orderType,InvestmentAccount investmentAccount,BigDecimal orderAmount){
        if (orderType.equals(OrderType.BUY)) {
            investmentAccount.setBalance(investmentAccount.getBalance().subtract(orderAmount));
        } else {
            investmentAccount.setBalance(investmentAccount.getBalance().add(orderAmount));
        }
        investmentAccountService.updateAccount(investmentAccount);
    }
}
