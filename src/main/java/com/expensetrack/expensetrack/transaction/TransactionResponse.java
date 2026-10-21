package com.expensetrack.expensetrack.transaction;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
public record TransactionResponse(
    UUID id,
    String title,
    BigDecimal amount,
    TransactionType transactionType,
    LocalDate date,
    Instant createdAt,
    Instant updatedAt
) {}