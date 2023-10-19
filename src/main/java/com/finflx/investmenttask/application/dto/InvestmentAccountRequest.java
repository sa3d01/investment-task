package com.finflx.investmenttask.application.dto;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Getter
@Setter
public class InvestmentAccountRequest {
    @NotNull(message = "Account number is required")
    @Size(min = 10, max = 30, message = "Account number must be between 10 and 30 characters")
    private String accountNumber;

    @NotNull(message = "Balance is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Balance must be greater than 0")
    private BigDecimal balance;
}
