package com.finflx.investmenttask.presentation.controller;

import com.finflx.investmenttask.application.dto.InvestmentAccountRequest;
import com.finflx.investmenttask.application.dto.InvestmentAccountResponse;
import com.finflx.investmenttask.application.service.InvestmentAccountService;
import com.finflx.investmenttask.domain.Modules;
import com.finflx.investmenttask.domain.Routes;
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
@RequestMapping(Routes.ACCOUNT_URL)
@Tag(name = Modules.ACCOUNT)
@SecurityRequirement(name = "bearerAuth")
public class InvestmentAccountController {
    private final InvestmentAccountService accountService;

    public InvestmentAccountController(InvestmentAccountService accountService) {
        this.accountService = accountService;
    }

    @Secured({INVESTOR_ROLE})
    @PostMapping
    public ResponseEntity<InvestmentAccountResponse> createAccount(@Valid @RequestBody InvestmentAccountRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(accountService.createAccount(request));
    }
}
