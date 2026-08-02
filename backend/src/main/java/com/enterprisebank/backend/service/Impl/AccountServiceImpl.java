package com.enterprisebank.backend.service.Impl;

import com.enterprisebank.backend.dto.AccountRequest;
import com.enterprisebank.backend.dto.AccountResponse;
import com.enterprisebank.backend.entity.Account;
import com.enterprisebank.backend.entity.Customer;
import com.enterprisebank.backend.exception.AccountNotFoundException;
import com.enterprisebank.backend.exception.CustomerNotFoundException;
import com.enterprisebank.backend.mapper.AccountMapper;
import com.enterprisebank.backend.repository.AccountRepository;
import com.enterprisebank.backend.repository.CustomerRepository;
import com.enterprisebank.backend.service.AccountService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final AccountMapper accountMapper;

    public AccountServiceImpl(AccountRepository accountRepository,
                              CustomerRepository customerRepository,
                              AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.accountMapper = accountMapper;
    }

    // Methods will go here
    @Override
    public AccountResponse createAccount(AccountRequest request) {

        // Find customer
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException(
                        "Customer not found with ID: " + request.getCustomerId()));

        // Convert DTO to Entity
        Account account = accountMapper.toEntity(request);

        // Generate account number
        account.setAccountNumber("ACC" + System.currentTimeMillis());

        // Default values
        account.setStatus("ACTIVE");
        account.setCreatedAt(LocalDateTime.now());

        // Associate customer
        account.setCustomer(customer);

        // Save account
        Account savedAccount = accountRepository.save(account);

        // Convert Entity to Response DTO
        return accountMapper.toResponse(savedAccount);
    }

    @Override
    public AccountResponse getAccountById(Long id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() ->
                        new AccountNotFoundException("Account not found with ID: " + id));

        return accountMapper.toResponse(account);
    }

    @Override
    public List<AccountResponse> getAllAccounts() {

        return accountRepository.findAll()
                .stream()
                .map(accountMapper::toResponse)
                .toList();
    }

    @Override
    public void deleteAccount(Long id) {

        if (!accountRepository.existsById(id)) {
            throw new AccountNotFoundException(
                    "Account not found with ID: " + id);
        }

        accountRepository.deleteById(id);
    }
}
