package com.finflx.investmenttask.application.dto;

import com.finflx.investmenttask.domain.enumuration.OrderStatus;
import com.finflx.investmenttask.domain.enumuration.OrderType;
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
public class InvestmentOrderResponse {
    private Long orderId;
    private InvestmentAccountResponse account;
    private BigDecimal orderAmount;
    private OrderType orderType;
    private OrderStatus orderStatus;
    private LocalDateTime createdAt;
}
