package com.expensetrack.expensetrack.transaction;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public TransactionResponse create(CreateTransactionRequest request) {
        Transaction transaction = new Transaction(
                request.title(),
                request.amount(),
                request.transactionType(),
                request.date()
        );

        Transaction savedTransaction = transactionRepository.save(transaction);
        return new TransactionResponse(
                savedTransaction.getId(),
                savedTransaction.getTitle(),
                savedTransaction.getAmount(),
                savedTransaction.getTransactionType(),
                savedTransaction.getDate(),
                savedTransaction.getCreatedAt(),
                savedTransaction.getUpdatedAt()
        );
    }

    @Transactional
    public List<TransactionResponse> getAll() {
        return transactionRepository.findAll()
                .stream()
                .map(transaction -> new TransactionResponse(
                        transaction.getId(),
                        transaction.getTitle(),
                        transaction.getAmount(),
                        transaction.getTransactionType(),
                        transaction.getDate(),
                        transaction.getCreatedAt(),
                        transaction.getUpdatedAt()
                ))
                .toList();
    }

    @Transactional
    public Page<TransactionResponse> search(
            TransactionType transactionType,
            LocalDate dateFrom,
            LocalDate dateTo,
            Pageable pageable
    ) {
        Specification<Transaction> specification = TransactionSpecifications.hasType(transactionType).and(TransactionSpecifications.dateFrom(dateFrom)).and(TransactionSpecifications.dateTo(dateTo));

        return transactionRepository.findAll(specification, pageable)
                .map(transaction -> new TransactionResponse(
                        transaction.getId(),
                        transaction.getTitle(),
                        transaction.getAmount(),
                        transaction.getTransactionType(),
                        transaction.getDate(),
                        transaction.getCreatedAt(),
                        transaction.getUpdatedAt()
                ));
    }

}
