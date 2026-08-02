package com.enterprisebank.backend.service;

import com.enterprisebank.backend.dto.AccountRequest;
import com.enterprisebank.backend.dto.AccountResponse;

import java.util.List;

public interface AccountService {

    AccountResponse createAccount(AccountRequest request);

    List<AccountResponse> getAllAccounts();

    AccountResponse getAccountById(Long id);

    void deleteAccount(Long id);
}
