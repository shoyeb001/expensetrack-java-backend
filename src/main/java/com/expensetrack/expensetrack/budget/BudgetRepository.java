package com.expensetrack.expensetrack.budget;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BudgetRepository extends JpaRepository<Budget, UUID> {
    // get all budget of a user
    Page<Budget> findByUserId(
            UUID userId,
            Pageable pageable);

    // get a specific budget of a user
    Optional<Budget> findByIdAndUserId(
            UUID budgetId,
            UUID userId);

    // get budgets by category
    Optional<Budget> findByUserIdAndCategoryId(
            UUID userId,
            UUID categoryId);

    List<Budget> findByUserIdAndCategoryIsNull(UUID userId);

    // check wheater an budget already exists

    @Query("""
                SELECT COUNT(b) > 0
                FROM Budget b
                WHERE b.user.id = :userId
                  AND (
                      (:categoryId IS NULL AND b.category IS NULL)
                      OR b.category.id = :categoryId
                  )
                  AND b.startDate <= :endDate
                  AND b.endDate >= :startDate
                  AND (:excludeBudgetId IS NULL OR b.id <> :excludeBudgetId)
            """)
    boolean existsOverlappingBudget(
            @Param("userId") UUID userId,
            @Param("categoryId") UUID categoryId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("excludeBudgetId") UUID excludeBudgetId);
}
