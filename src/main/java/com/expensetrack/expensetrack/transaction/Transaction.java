package com.expensetrack.expensetrack.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.expensetrack.expensetrack.common.entity.BaseEntity;
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
@Table(name = "transactions")
public class Transaction extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)

    private String title;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false, length = 20)
    private TransactionType transactionType;

    @Column(name = "transaction_date", nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    protected Transaction() {
        // Default constructor for JPA
    }

    public Transaction(String title, BigDecimal amount, TransactionType transactionType, LocalDate date) {
        this.title = title;
        this.amount = amount;
        this.transactionType = transactionType;
        this.date = date;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setAmount(BigDecimal amount){
        this.amount = amount;
    }

    public void setTransactionType(TransactionType transactionType){
        this.transactionType = transactionType;
    }

    public void setDate(LocalDate date){
        this.date = date;
    }
}
