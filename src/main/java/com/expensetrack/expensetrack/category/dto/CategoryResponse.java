package com.expensetrack.expensetrack.category.dto;

import java.time.Instant;
import java.util.UUID;

import com.expensetrack.expensetrack.transaction.TransactionType;

public record CategoryResponse(
        UUID id,
        String name,
        TransactionType category_type,
        boolean is_System,
        String icon,
        Instant createdAt,
        Instant updatedAt) {
}