package com.enterprisebank.backend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class AccountRequest {

    @NotBlank(message = "Account type is required")
    private String accountType;

    @NotNull(message = "Initial balance is required")
    @DecimalMin(value = "0.00", message = "Balance cannot be negative")
    private BigDecimal balance;

    @NotBlank(message = "IFSC code is required")
    private String ifscCode;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    public AccountRequest() {
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
