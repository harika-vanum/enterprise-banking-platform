package com.enterprisebank.backend.mapper;

import com.enterprisebank.backend.dto.AccountRequest;
import com.enterprisebank.backend.dto.AccountResponse;
import com.enterprisebank.backend.entity.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    // Request DTO -> Entity
    public Account toEntity(AccountRequest request) {

        Account account = new Account();

        account.setAccountType(request.getAccountType());
        account.setBalance(request.getBalance());
        account.setIfscCode(request.getIfscCode());

        return account;
    }

    // Entity -> Response DTO
    public AccountResponse toResponse(Account account) {

        AccountResponse response = new AccountResponse();

        response.setId(account.getId());
        response.setAccountNumber(account.getAccountNumber());
        response.setAccountType(account.getAccountType());
        response.setBalance(account.getBalance());
        response.setIfscCode(account.getIfscCode());
        response.setStatus(account.getStatus());
        response.setCreatedAt(account.getCreatedAt());

        if (account.getCustomer() != null) {
            response.setCustomerId(account.getCustomer().getId());
        }

        return response;
    }
}
