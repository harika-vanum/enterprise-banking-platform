package com.enterprisebank.backend.mapper;

import com.enterprisebank.backend.dto.TransactionResponse;
import com.enterprisebank.backend.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionResponse toResponse(Transaction transaction) {

        TransactionResponse response = new TransactionResponse();

        response.setId(transaction.getId());

        if (transaction.getFromAccount() != null) {
            response.setFromAccountId(transaction.getFromAccount().getId());
        }

        if (transaction.getToAccount() != null) {
            response.setToAccountId(transaction.getToAccount().getId());
        }

        response.setTransactionType(transaction.getTransactionType());
        response.setAmount(transaction.getAmount());
        response.setBalanceAfterTransaction(
                transaction.getBalanceAfterTransaction()
        );
        response.setDescription(transaction.getDescription());
        response.setTransactionReference(
                transaction.getTransactionReference()
        );
        response.setTransactionDate(transaction.getTransactionDate());
        response.setStatus(transaction.getStatus());

        return response;
    }
}
