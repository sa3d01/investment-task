package com.finflx.investmenttask.presentation.controller;

import com.finflx.investmenttask.domain.Modules;
import com.finflx.investmenttask.domain.Routes;
import com.finflx.investmenttask.application.dto.InvestmentOrderRequest;
import com.finflx.investmenttask.application.dto.InvestmentOrderResponse;
import com.finflx.investmenttask.application.service.InvestmentOrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.finflx.investmenttask.infrastructure.util.SecurityUtils.INVESTOR_ROLE;

@RestController
@RequestMapping(Routes.ORDER_URL)
@Tag(name = Modules.ORDER)
@SecurityRequirement(name = "bearerAuth")
public class InvestmentOrderController {
    private final InvestmentOrderService orderService;

    public InvestmentOrderController(InvestmentOrderService orderService) {
        this.orderService = orderService;
    }

    @Secured({INVESTOR_ROLE})
    @PostMapping
    public ResponseEntity<InvestmentOrderResponse> placeOrder(@Valid @RequestBody InvestmentOrderRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderService.placeOrder(request));
    }
}
