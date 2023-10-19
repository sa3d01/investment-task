package com.finflx.investmenttask.application.service;

import com.finflx.investmenttask.application.dto.InvestmentAccountRequest;
import com.finflx.investmenttask.application.dto.InvestmentAccountResponse;
import com.finflx.investmenttask.domain.entity.InvestmentAccount;
import com.finflx.investmenttask.application.mapper.InvestmentAccountMapper;
import com.finflx.investmenttask.domain.repository.InvestmentAccountRepository;
import com.finflx.investmenttask.domain.entity.User;
import com.finflx.investmenttask.infrastructure.util.SecurityUtils;
import com.finflx.investmenttask.presentation.exception.EntityNotFoundException;
import com.finflx.investmenttask.presentation.exception.NotEligibleToUpdateEntityException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InvestmentAccountService {
    private final InvestmentAccountRepository investmentAccountRepository;
    private final UserService userService;

    public InvestmentAccountService(InvestmentAccountRepository investmentAccountRepository, UserService userService) {
        this.investmentAccountRepository = investmentAccountRepository;
        this.userService = userService;
    }

    @Transactional
    public InvestmentAccountResponse createAccount(InvestmentAccountRequest request) {
        Long authUserId = SecurityUtils.getAuthUserId();
        userService.validateUserExistingAndHasInvestorRole(authUserId);
        User owner = userService.getById(authUserId);
        InvestmentAccount account = InvestmentAccountMapper.INSTANCE.map(request);
        account.setOwner(owner);
        return InvestmentAccountMapper.INSTANCE.map(investmentAccountRepository.save(account));
    }

    @Transactional
    public void updateAccount(InvestmentAccount account) {
        investmentAccountRepository.save(account);
    }

    public void validateAccountBelongsToUser(Long accountId, Long userId) {
        InvestmentAccount account = investmentAccountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new EntityNotFoundException(InvestmentAccount.class.getSimpleName(), "id", accountId));

        if (!account.getOwner().getId().equals(userId)) {
            throw new NotEligibleToUpdateEntityException("The user with id: " + userId + " is not eligible to update account with id: " + accountId);
        }
    }

    public InvestmentAccount getById(Long id) {
        return investmentAccountRepository.findByAccountId(id)
                .orElseThrow(() -> new EntityNotFoundException(InvestmentAccount.class.getSimpleName(), "id", id));
    }
}
