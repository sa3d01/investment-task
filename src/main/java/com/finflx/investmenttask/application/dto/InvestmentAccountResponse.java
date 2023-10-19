package com.finflx.investmenttask.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InvestmentAccountResponse {
    private Long accountId;
    private String accountNumber;
    private BigDecimal balance;
    private LocalDateTime createdAt;
}
