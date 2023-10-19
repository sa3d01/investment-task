package com.finflx.investmenttask.domain.repository;

import com.finflx.investmenttask.domain.entity.InvestmentOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestmentOrderRepository extends JpaRepository<InvestmentOrder, Long> {
}
