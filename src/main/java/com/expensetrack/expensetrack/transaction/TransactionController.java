package com.expensetrack.expensetrack.transaction;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import org.springframework.web.bind.annotation.RequestParam;


@RestController 
@RequestMapping("/api/v1/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(
        TransactionService transactionService
    ){
        this.transactionService = transactionService;
    }

    @PostMapping 
    @ResponseStatus(HttpStatus.CREATED)

    public TransactionResponse create(
        @Valid @RequestBody CreateTransactionRequest request
    ){
        return transactionService.create(request);
    }
    
    @GetMapping()
    // public List<TransactionResponse> findAll(){
    //     return transactionService.getAll();
    // }
    public Page<TransactionResponse> search(
        @RequestParam(required = false) TransactionType transactionType,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
        @PageableDefault(sort = "date", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return transactionService.search(transactionType, dateFrom, dateTo, pageable);
    }


}
