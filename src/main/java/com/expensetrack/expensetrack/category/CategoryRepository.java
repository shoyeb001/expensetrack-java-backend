package com.expensetrack.expensetrack.category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.expensetrack.expensetrack.transaction.TransactionType;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
        // fetch system categories and the current user categories

        // List<Category> findBySystemTrueOrUser_IdOrderByNameAsc(UUID userId);

        // // fetch system + user categories filter by type
        // List<Category>
        // findBySystemTrueAndCategoryTypeOrUser_IdCategoryTypeOrderByNameAsc(
        // UUID systemUserId,
        // TransactionType type,
        // UUID userId,
        // TransactionType userType);

        // Check whether a custom category already exists.
        boolean existsByUserIdAndNameIgnoreCaseAndCategoryType(
                        UUID userId,
                        String name,
                        TransactionType categoryType);

        // Find a category only if it is system-owned or belongs to the user.
        @Query("""
                        SELECT c FROM Category c
                        WHERE c.id = :id
                        AND (c.isSystem = true OR c.user.id = :userId)
                        """)
        Optional<Category> findByIdAndAvailableToUser(
                        @Param("id") UUID id,
                        @Param("userId") UUID userId);

        Optional<Category> findByIdAndIsSystemTrueOrIdAndUserId(
                        UUID systemCategoryId,
                        UUID customCategoryId,
                        UUID userId);

        @Query("""
                        SELECT c FROM Category c
                        WHERE c.isSystem = true OR c.user.id = :userId
                        ORDER BY c.categoryType, c.name
                        """)
        List<Category> findAvailableCategoriesForUser(
                        @Param("userId") UUID userId);

        @Query("""
                        SELECT c FROM Category c
                        WHERE (c.isSystem = true OR c.user.id = :userId)
                        AND c.categoryType = :type
                        ORDER BY c.name
                        """)
        List<Category> findAvailableToUserByType(
                        @Param("userId") UUID userId,
                        @Param("type") TransactionType type);

}
