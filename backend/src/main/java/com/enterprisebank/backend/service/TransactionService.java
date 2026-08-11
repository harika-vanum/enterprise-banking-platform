package com.enterprisebank.backend.service;

import com.enterprisebank.backend.dto.TransactionRequest;
import com.enterprisebank.backend.dto.TransactionResponse;

import java.util.List;

public interface TransactionService {

    TransactionResponse createTransaction(TransactionRequest request);

    TransactionResponse getTransactionById(Long id);

    TransactionResponse getTransactionByReference(String transactionReference);

    List<TransactionResponse> getAllTransactions();

    List<TransactionResponse> getTransactionsByAccountId(Long accountId);
}
