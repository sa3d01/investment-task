package com.finflx.investmenttask.domain.repository;

import com.finflx.investmenttask.domain.entity.InvestmentAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvestmentAccountRepository extends JpaRepository<InvestmentAccount, Long> {
    Optional<InvestmentAccount> findByAccountId(Long accountId);
}
