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
    private TransactionType category_type;

    @Column(name = "is_System", nullable = false)
    private boolean is_system;

    @Column(length = 50)
    private String icon;

    protected Category() {

    }

    public Category(
            User user,
            String name,
            TransactionType category_type,
            boolean is_system,
            String icon) {
        this.user = user;
        this.name = name;
        this.category_type = category_type;
        this.is_system = is_system;
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

    public TransactionType getType() {
        return category_type;
    }

    public String getIcon() {
        return icon;
    }

    public boolean getSystem() {
        return is_system;
    }

}
