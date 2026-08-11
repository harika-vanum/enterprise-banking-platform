package com.enterprisebank.backend.service.Impl;

import com.enterprisebank.backend.mapper.TransactionMapper;
import com.enterprisebank.backend.dto.TransactionRequest;
import com.enterprisebank.backend.dto.TransactionResponse;
import com.enterprisebank.backend.entity.Account;
import com.enterprisebank.backend.entity.Transaction;
import com.enterprisebank.backend.entity.TransactionStatus;
import com.enterprisebank.backend.entity.TransactionType;
import com.enterprisebank.backend.repository.AccountRepository;
import com.enterprisebank.backend.repository.TransactionRepository;
import com.enterprisebank.backend.service.TransactionService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;

    public TransactionServiceImpl(
            TransactionRepository transactionRepository,
            AccountRepository accountRepository,
            TransactionMapper transactionMapper) {

        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.transactionMapper = transactionMapper;
    }

    @Override
    @Transactional
    public TransactionResponse createTransaction(TransactionRequest request) {

        validateAmount(request.getAmount());

        TransactionType transactionType = request.getTransactionType();

        if (transactionType == null) {
            throw new IllegalArgumentException("Transaction type is required");
        }

        Account fromAccount = null;
        Account toAccount = null;

        /*
         * DEPOSIT
         */
        if (transactionType == TransactionType.DEPOSIT) {

            if (request.getToAccountId() == null) {
                throw new IllegalArgumentException(
                        "Account ID is required for deposit"
                );
            }

            toAccount = getAccount(request.getToAccountId());

            toAccount.setBalance(
                    toAccount.getBalance().add(request.getAmount())
            );

            accountRepository.save(toAccount);
        }

        /*
         * WITHDRAWAL
         */
        else if (transactionType == TransactionType.WITHDRAWAL) {

            if (request.getFromAccountId() == null) {
                throw new IllegalArgumentException(
                        "Account ID is required for withdrawal"
                );
            }

            fromAccount = getAccount(request.getFromAccountId());

            validateSufficientBalance(
                    fromAccount,
                    request.getAmount()
            );

            fromAccount.setBalance(
                    fromAccount.getBalance().subtract(request.getAmount())
            );

            accountRepository.save(fromAccount);
        }

        /*
         * TRANSFER
         */
        else if (transactionType == TransactionType.TRANSFER) {

            if (request.getFromAccountId() == null ||
                    request.getToAccountId() == null) {

                throw new IllegalArgumentException(
                        "Both from account and to account are required for transfer"
                );
            }

            if (request.getFromAccountId()
                    .equals(request.getToAccountId())) {

                throw new IllegalArgumentException(
                        "From account and to account cannot be the same"
                );
            }

            fromAccount = getAccount(request.getFromAccountId());
            toAccount = getAccount(request.getToAccountId());

            validateSufficientBalance(
                    fromAccount,
                    request.getAmount()
            );

            fromAccount.setBalance(
                    fromAccount.getBalance().subtract(request.getAmount())
            );

            toAccount.setBalance(
                    toAccount.getBalance().add(request.getAmount())
            );

            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);
        }

        /*
         * Create transaction record
         */
        Transaction transaction = new Transaction();

        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setTransactionType(transactionType);
        transaction.setAmount(request.getAmount());

        /*
         * Balance after transaction:
         *
         * For withdrawal / transfer → from account balance
         * For deposit → to account balance
         */
        if (transactionType == TransactionType.DEPOSIT) {

            transaction.setBalanceAfterTransaction(
                    toAccount.getBalance()
            );

        } else {

            transaction.setBalanceAfterTransaction(
                    fromAccount.getBalance()
            );
        }

        transaction.setDescription(request.getDescription());

        transaction.setTransactionReference(
                generateTransactionReference()
        );

        transaction.setTransactionDate(
                LocalDateTime.now()
        );

        transaction.setStatus(
                TransactionStatus.SUCCESS
        );

        Transaction savedTransaction =
                transactionRepository.save(transaction);

        return transactionMapper.toResponse(savedTransaction);
    }

    @Override
    public TransactionResponse getTransactionById(Long id) {

        Transaction transaction =
                transactionRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Transaction not found with id: " + id
                                ));

        return transactionMapper.toResponse(transaction);
    }

    @Override
    public TransactionResponse getTransactionByReference(
            String transactionReference) {

        Transaction transaction =
                transactionRepository
                        .findByTransactionReference(transactionReference)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Transaction not found with reference: "
                                                + transactionReference
                                ));

        return transactionMapper.toResponse(transaction);
    }

    @Override
    public List<TransactionResponse> getAllTransactions() {

        return transactionRepository.findAll()
                .stream()
                .map(transactionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionResponse> getTransactionsByAccountId(
            Long accountId) {

        return transactionRepository.findAll()
                .stream()
                .filter(transaction ->
                        (transaction.getFromAccount() != null &&
                                transaction.getFromAccount().getId()
                                        .equals(accountId))
                                ||
                                (transaction.getToAccount() != null &&
                                        transaction.getToAccount().getId()
                                                .equals(accountId))
                )
                .map(transactionMapper::toResponse)
                .collect(Collectors.toList());
    }

    private Account getAccount(Long accountId) {

        return accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Account not found with id: " + accountId
                        ));
    }

    private void validateAmount(BigDecimal amount) {

        if (amount == null ||
                amount.compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "Transaction amount must be greater than zero"
            );
        }
    }

    private void validateSufficientBalance(
            Account account,
            BigDecimal amount) {

        if (account.getBalance() == null ||
                account.getBalance().compareTo(amount) < 0) {

            throw new IllegalArgumentException(
                    "Insufficient account balance"
            );
        }
    }

    private String generateTransactionReference() {

        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        String uniquePart = UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();

        return "TXN-" + timestamp + "-" + uniquePart;
    }
}
