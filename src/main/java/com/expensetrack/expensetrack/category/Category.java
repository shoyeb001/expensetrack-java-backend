package com.expensetrack.expensetrack.category;

import java.util.UUID;

import com.expensetrack.expensetrack.common.entity.BaseEntity;
import com.expensetrack.expensetrack.transaction.TransactionType;
import com.expensetrack.expensetrack.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "categories")
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "category_type", nullable = false, length = 20)
    private TransactionType categoryType;

    @Column(name = "is_system", nullable = false)
    private boolean isSystem;

    @Column(length = 50)
    private String icon;

    protected Category() {

    }

    public Category(
            User user,
            String name,
            TransactionType categoryType,
            boolean isSystem,
            String icon) {
        this.user = user;
        this.name = name;
        this.categoryType = categoryType;
        this.isSystem = isSystem;
        this.icon = icon;
    }

    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getName() {
        return name;
    }

    public TransactionType getCategoryType() {
        return categoryType;
    }

    public TransactionType getType() {
        return categoryType;
    }

    public String getIcon() {
        return icon;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public boolean getSystem() {
        return isSystem;
    }

}
