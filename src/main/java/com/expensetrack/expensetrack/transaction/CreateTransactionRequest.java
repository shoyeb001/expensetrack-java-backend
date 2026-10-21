package com.expensetrack.expensetrack.transaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.math.BigDecimal;

public record CreateTransactionRequest(
    @NotBlank(message="Title is required") @Size(max=100, message="Title must be less than 100 characters") String title,
    @NotNull(message="Amount is required") @Positive(message="Amount must be greater than 0") BigDecimal amount,
    @NotNull(message="Transaction type is required") TransactionType transactionType,
    @NotNull(message="Date is required") 
    @PastOrPresent(message="Date cannot be in the future") LocalDate date
) {}