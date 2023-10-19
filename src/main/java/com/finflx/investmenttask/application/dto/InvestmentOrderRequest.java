package com.finflx.investmenttask.application.dto;

import com.finflx.investmenttask.domain.enumuration.OrderType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class InvestmentOrderRequest {
    @NotNull(message = "Account Id is required")
    private Long accountId;

    @NotNull(message = "Order type is required")
    private OrderType orderType;

    @NotNull(message = "Order amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
    private BigDecimal orderAmount;
}
