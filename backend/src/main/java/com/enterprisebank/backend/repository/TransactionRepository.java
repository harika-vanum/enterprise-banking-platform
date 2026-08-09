package com.enterprisebank.backend.repository;

import com.enterprisebank.backend.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByFromAccountId(Long accountId);

    List<Transaction> findByToAccountId(Long accountId);

    List<Transaction> findByFromAccountIdOrToAccountId(
            Long fromAccountId,
            Long toAccountId
    );
}
