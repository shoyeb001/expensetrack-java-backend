package com.expensetrack.expensetrack.transaction;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TransactionRepository
                extends JpaRepository<Transaction, UUID>,
                JpaSpecificationExecutor<Transaction> {
        Page<Transaction> findAllByUserId(UUID userId, Pageable pageable);

        Optional<Transaction> findByIdAndUserId(UUID transactionId, UUID userId);

        boolean existsByIdAndUserId(UUID transactionId, UUID userId);
}