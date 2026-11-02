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
        boolean existsByUser_IdAndNameIgnoreCaseAndCategory_type(
                        UUID userId,
                        String name,
                        TransactionType categoryType);

        // Find a category only if it is system-owned or belongs to the user.
        Optional<Category> findByIdAndSystemTrueOrIdAndUser_Id(
                        UUID systemCategoryId,
                        UUID customCategoryId,
                        UUID userId);

        @Query("""
                        SELECT c FROM Category c
                        WHERE c.is_system = true OR c.user.id = :userId
                        ORDER BY c.category_type, c.name
                        """)
        List<Category> findAvailableCategoriesForUser(
                        @Param("userId") UUID userId);

        @Query("""
                            SELECT c FROM Category c
                            WHERE (c.system = true OR c.user.id = :userId)
                            AND c.category_type = :type
                            ORDER BY c.name
                        """)
        List<Category> findAvailableToUserByType(
                        @Param("userId") UUID userId,
                        @Param("type") TransactionType type);

}
