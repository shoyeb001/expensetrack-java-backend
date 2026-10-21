package com.expensetrack.expensetrack.transaction;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

public final class TransactionSpecifications {

    public TransactionSpecifications() {
    }

    public static Specification<Transaction> hasType(TransactionType transactionType) {
        return (root, query, criteriaBuilder) -> transactionType == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("transactionType"), transactionType);
    }

    // This method returns a Specification that checks if the transactionType field of the Transaction entity matches the provided transactionType parameter. If the parameter is null, it returns a conjunction (always true) to avoid filtering by type.
    public static Specification<Transaction> dateFrom(LocalDate dateFrom) {
        return (root, query, criteriaBuilder) -> dateFrom == null ? criteriaBuilder.conjunction() : criteriaBuilder.greaterThanOrEqualTo(root.get("date"), dateFrom);
    }

    // This method returns a Specification that checks if the date field of the Transaction entity is greater than or equal to the provided dateFrom parameter. If the parameter is null, it returns a conjunction (always true) to avoid filtering by date.

    public static Specification<Transaction> dateTo(LocalDate dateTo) {
        return (root, query, criteriaBuilder) -> dateTo == null ? criteriaBuilder.conjunction() : criteriaBuilder.lessThanOrEqualTo(root.get("date"), dateTo);
    }
    

}
