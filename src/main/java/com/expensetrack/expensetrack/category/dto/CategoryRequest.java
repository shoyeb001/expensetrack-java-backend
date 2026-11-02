package com.expensetrack.expensetrack.category.dto;

import com.expensetrack.expensetrack.transaction.TransactionType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
        @NotBlank(message = "Category name is required") @Size(max = 100, message = "Category name cannot exists 100 characters") String name,

        @NotNull(message = "category type is required") TransactionType category_type,

        @Size(max = 50, message = "Icon cannot exceed 50 character") String icon) {

}
