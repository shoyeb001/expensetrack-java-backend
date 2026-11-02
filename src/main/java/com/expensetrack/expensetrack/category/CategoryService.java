package com.expensetrack.expensetrack.category;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.expensetrack.expensetrack.category.dto.CategoryRequest;
import com.expensetrack.expensetrack.category.dto.CategoryResponse;
import com.expensetrack.expensetrack.transaction.TransactionType;
import com.expensetrack.expensetrack.user.User;
import com.expensetrack.expensetrack.user.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CategoryService(
            CategoryRepository categoryRepository,
            UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public CategoryResponse create(
            CategoryRequest request,
            UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        String name = request.name().trim();

        boolean exists = categoryRepository.existsByUser_IdAndNameIgnoreCaseAndCategory_type(userId, name,
                request.category_type());

        if (exists) {
            throw new IllegalArgumentException(
                    "You already have a category with this name and type");
        }

        Category category = new Category(
                user,
                name,
                request.category_type(),
                false,
                request.icon());

        Category savedCategory = categoryRepository.save(category);
        return new CategoryResponse(
                savedCategory.getId(),
                savedCategory.getName(),
                savedCategory.getType(),
                savedCategory.getSystem(),
                savedCategory.getIcon(),
                savedCategory.getCreatedAt(),
                savedCategory.getUpdatedAt());

    }

    @Transactional
    public List<CategoryResponse> findAll(
            UUID userId,
            TransactionType type) {
        List<Category> categories;

        if (type == null) {
            categories = categoryRepository.findAvailableCategoriesForUser(userId);
        } else {
            categories = categoryRepository.findAvailableToUserByType(userId, type);
        }

        return categories.stream()
                .map(category -> new CategoryResponse(
                        category.getId(),
                        category.getName(),
                        category.getType(),
                        category.getSystem(),
                        category.getIcon(),
                        category.getCreatedAt(),
                        category.getUpdatedAt()))
                .toList();
    }
}
