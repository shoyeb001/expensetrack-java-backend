package com.expensetrack.expensetrack.transaction;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.apache.catalina.connector.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(
            TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)

    public TransactionResponse create(
            @Valid @RequestBody CreateTransactionRequest request, @AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return transactionService.create(request, userId);
    }

    @GetMapping()
    // public List<TransactionResponse> findAll(){
    // return transactionService.getAll();
    // }
    public Page<TransactionResponse> search(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(required = false) TransactionType transactionType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
            @PageableDefault(sort = "date", direction = Sort.Direction.DESC) Pageable pageable) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return transactionService.search(userId, transactionType, dateFrom, dateTo, pageable);
    }

    @PutMapping("/{id}")
    public TransactionResponse updateTransaction(@PathVariable UUID id,
            @Valid @RequestBody UpdateTransactionRequest request, @AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return transactionService.update(id, userId, request);
    }

    @DeleteMapping ("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable UUID id, @AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        transactionService.delete(id, userId);
        return ResponseEntity.noContent().build();
    }

}
