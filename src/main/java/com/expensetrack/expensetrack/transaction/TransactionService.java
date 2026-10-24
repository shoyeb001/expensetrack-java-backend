package com.expensetrack.expensetrack.transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.expensetrack.expensetrack.user.User;
import com.expensetrack.expensetrack.user.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class TransactionService {

        private final TransactionRepository transactionRepository;
        private final UserRepository userRepository;

        public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository) {
                this.transactionRepository = transactionRepository;
                this.userRepository = userRepository;
        }

        @Transactional
        public TransactionResponse create(CreateTransactionRequest request, UUID userId) {
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
                Transaction transaction = new Transaction(
                                request.title(),
                                request.amount(),
                                request.transactionType(),
                                request.date());
                transaction.setUser(user);

                Transaction savedTransaction = transactionRepository.save(transaction);
                return new TransactionResponse(
                                savedTransaction.getId(),
                                savedTransaction.getTitle(),
                                savedTransaction.getAmount(),
                                savedTransaction.getTransactionType(),
                                savedTransaction.getDate(),
                                savedTransaction.getCreatedAt(),
                                savedTransaction.getUpdatedAt());
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
                                                transaction.getUpdatedAt()))
                                .toList();
        }

        @Transactional
        public Page<TransactionResponse> search(
                        UUID userId,
                        TransactionType transactionType,
                        LocalDate dateFrom,
                        LocalDate dateTo,
                        Pageable pageable) {
                Specification<Transaction> specification = TransactionSpecifications.belongsToUser(userId)
                                .and(TransactionSpecifications.hasType(transactionType))
                                .and(TransactionSpecifications.dateFrom(dateFrom))
                                .and(TransactionSpecifications.dateTo(dateTo));

                return transactionRepository.findAll(specification, pageable)
                                .map(transaction -> new TransactionResponse(
                                                transaction.getId(),
                                                transaction.getTitle(),
                                                transaction.getAmount(),
                                                transaction.getTransactionType(),
                                                transaction.getDate(),
                                                transaction.getCreatedAt(),
                                                transaction.getUpdatedAt()));
        }

        @Transactional
        public TransactionResponse update(UUID id, UUID userId, UpdateTransactionRequest request) {
                Transaction transaction = transactionRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Transaction not found with id: " + id));

                if (!transaction.getUser().getId().equals(userId)) {
                        throw new IllegalArgumentException(
                                        "Transaction does not belong to the user with id: " + userId);
                }

                transaction.setTitle(request.title());
                transaction.setAmount(request.amount());
                transaction.setTransactionType(request.transactionType());
                transaction.setDate(request.date());

                Transaction updatedTransaction = transactionRepository.save(transaction);

                return new TransactionResponse(
                                updatedTransaction.getId(),
                                updatedTransaction.getTitle(),
                                updatedTransaction.getAmount(),
                                updatedTransaction.getTransactionType(),
                                updatedTransaction.getDate(),
                                updatedTransaction.getCreatedAt(),
                                updatedTransaction.getUpdatedAt());
        }

        @Transactional
        public void delete(UUID id, UUID userId) {
                Transaction transaction = transactionRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Transaction not found with id: " + id));

                if (!transaction.getUser().getId().equals(userId)) {
                        throw new IllegalArgumentException(
                                        "Transaction does not belong to the user with id: " + userId);
                }

                transactionRepository.delete(transaction);
        }

        @Transactional
        public TransactionResponse findById(UUID id, UUID userId){
                Transaction transaction = transactionRepository.findByIdAndUserId(id, userId).orElseThrow(() -> new EntityNotFoundException("Transaction not found"));
                return new TransactionResponse(
                        transaction.getId(),
                        transaction.getTitle(),
                        transaction.getAmount(),               
                        transaction.getTransactionType(),
                        transaction.getDate(),
                        transaction.getCreatedAt(),
                        transaction.getUpdatedAt());
                
        }

}
